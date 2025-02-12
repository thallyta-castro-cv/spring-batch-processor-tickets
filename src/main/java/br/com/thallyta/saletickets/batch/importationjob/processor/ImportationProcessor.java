package br.com.thallyta.saletickets.batch.importationjob.processor;

import br.com.thallyta.saletickets.domain.Importation;
import org.springframework.batch.item.ItemProcessor;

public class ImportationProcessor implements ItemProcessor<Importation, Importation> {

    @Override
    public Importation process(Importation item) throws Exception {
        if (item.getTypeTicket().equalsIgnoreCase("vip")) {
            item.setAdministrationFee(130.0);
        } else if (item.getTypeTicket().equalsIgnoreCase("camarote")) {
            item.setAdministrationFee(80.0);
        } else {
            item.setAdministrationFee(50.0);
        }

        return item;
    }
}
