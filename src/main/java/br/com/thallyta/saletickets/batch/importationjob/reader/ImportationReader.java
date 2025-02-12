package br.com.thallyta.saletickets.batch.importationjob.reader;

import br.com.thallyta.saletickets.domain.Importation;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public class ImportationReader{

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
}
