package br.com.thallyta.saletickets.batch.job;

import br.com.thallyta.saletickets.batch.job.processor.ImportationProcessor;
import br.com.thallyta.saletickets.batch.job.reader.ImportationMapper;
import br.com.thallyta.saletickets.common.FileMoveException;
import br.com.thallyta.saletickets.domain.Importation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@Configuration
@Slf4j
public class ImportationJobConfiguration {

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public Job job(Step initialStep, JobRepository jobRepository) {
        return new JobBuilder("ticket-generation", jobRepository)
                .start(initialStep)
                .next(moveFilesStep(jobRepository))
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step initialStep(ItemReader<Importation> reader, ItemWriter<Importation> writer, JobRepository jobRepository) {
        return new StepBuilder("initial-step", jobRepository)
                .<Importation, Importation>chunk(200, transactionManager)
                .reader(reader)
                .processor(processor())
                .writer(writer)
                .build();
    }

    @Bean
    public ItemReader<Importation> reader() {
        return new FlatFileItemReaderBuilder<Importation>()
                .name("reader-csv")
                .resource(new FileSystemResource("files/dados.csv"))
                .comments("--")
                .delimited()
                .delimiter(";")
                .names("cpf", "client", "birthDate", "event", "date", "typeTicket", "value")
                .fieldSetMapper(new ImportationMapper())
                .build();
    }

    @Bean
    public ItemWriter<Importation> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Importation>()
                .dataSource(dataSource)
                .sql(
                        "INSERT INTO importation (cpf, client, birth_date, event, date, type_ticket, value, importation_date, administration_fee) VALUES" +
                                " (:cpf, :client, :birthDate, :event, :date, :typeTicket, :value, :importationDate, :administrationFee)"

                )
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .build();
    }

    @Bean
    public ImportationProcessor processor(){
        return new ImportationProcessor();
    }

    @Bean
    public Tasklet moveFilesTasklet() {
        return (contribution, chunkContext) -> {
            Path sourceFolder = Paths.get("files");
            Path destinationFolder = Paths.get("imported-files");

            try {
                if (!Files.exists(sourceFolder) || !Files.isDirectory(sourceFolder)) {
                    log.warn("Source folder '{}' does not exist or is not a directory. No files to move.", sourceFolder);
                    return RepeatStatus.FINISHED;
                }

                Files.createDirectories(destinationFolder);

                try (DirectoryStream<Path> stream = Files.newDirectoryStream(sourceFolder, "*.csv")) {
                    for (Path file : stream) {
                        Path destinationFile = destinationFolder.resolve(file.getFileName());
                        try {
                            Files.move(file, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                            log.info("File moved: {}", file.getFileName());
                        } catch (IOException e) {
                            log.error("Failed to move file: {}", file.getFileName(), e);
                            throw new FileMoveException("Failed to move file: " + file.getFileName(), e);
                        }
                    }
                }
            } catch (IOException e) {
                log.error("Error handling file movement", e);
                throw new FileMoveException("Error handling file movement", e);
            }

            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Step moveFilesStep(JobRepository jobRepository) {
        return new StepBuilder("move-file", jobRepository)
                .tasklet(moveFilesTasklet(), transactionManager)
                .build();
    }
}
