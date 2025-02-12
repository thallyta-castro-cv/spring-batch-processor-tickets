package br.com.thallyta.saletickets.batch.importationjob.writer;

import br.com.thallyta.saletickets.domain.Importation;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class ImportationWriter {

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
}
