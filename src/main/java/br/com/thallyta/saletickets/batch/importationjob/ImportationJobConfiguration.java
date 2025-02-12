package br.com.thallyta.saletickets.batch.importationjob;

import br.com.thallyta.saletickets.batch.importationjob.processor.ImportationProcessor;
import br.com.thallyta.saletickets.batch.importationjob.tasklet.MoveFilesTasklet;
import br.com.thallyta.saletickets.domain.Importation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class ImportationJobConfiguration {

    @Autowired
    private PlatformTransactionManager transactionManager;

    private final MoveFilesTasklet tasklet;

    public ImportationJobConfiguration(MoveFilesTasklet tasklet) {
        this.tasklet = tasklet;
    }

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
    public ImportationProcessor processor(){
        return new ImportationProcessor();
    }

    @Bean
    public Step moveFilesStep(JobRepository jobRepository) {
        return new StepBuilder("move-file", jobRepository)
                .tasklet(tasklet.moveFilesTasklet(), transactionManager)
                .build();
    }
}
