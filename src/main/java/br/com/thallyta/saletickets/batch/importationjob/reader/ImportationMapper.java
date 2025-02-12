package br.com.thallyta.saletickets.batch.importationjob.reader;

import br.com.thallyta.saletickets.domain.Importation;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ImportationMapper implements FieldSetMapper<Importation> {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public Importation mapFieldSet(FieldSet fieldSet) {
        Importation importation = new Importation();
        importation.setCpf(fieldSet.readString("cpf"));
        importation.setClient(fieldSet.readString("client"));
        importation.setBirthDate(LocalDate.parse(fieldSet.readString("birthDate"), dateFormatter));
        importation.setEvent(fieldSet.readString("event"));
        importation.setDate(LocalDate.parse(fieldSet.readString("date"), dateFormatter));
        importation.setTypeTicket(fieldSet.readString("typeTicket"));
        importation.setValue(fieldSet.readDouble("value"));
        importation.setImportationDate(LocalDateTime.now());
        return importation;
    }
}
