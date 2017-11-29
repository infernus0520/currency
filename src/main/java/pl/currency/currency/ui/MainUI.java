package pl.currency.currency.ui;

import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.xml.sax.SAXException;
import pl.currency.currency.model.Rate;
import pl.currency.currency.repository.NbpApiService;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Title("Currency")
@SpringUI
public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        NbpApiService nbpApiService = new NbpApiService();

        HorizontalLayout hl = new HorizontalLayout();
        VerticalLayout lb = new VerticalLayout();
        VerticalLayout vl = new VerticalLayout();

        DateField startDate = new DateField("Start Date");
        DateField endDate = new DateField("End Date");

        TextField currency = new TextField("Currency");

        Button check = new Button("Check");

        Label average = new Label();
        Label sd = new Label();

        Grid<Rate> grid = new Grid<>();

        List<Rate> rates = new ArrayList<>();

        average.setCaption("Average");
        sd.setCaption("Standard Deviaton");
        grid.addColumn(Rate::getDate).setCaption("Date");
        grid.addColumn(Rate::getMid).setCaption("Average");


        lb.addComponents(average,sd);
        vl.addComponents(currency,startDate,endDate,check);
        hl.addComponents(vl,grid,lb);
        setContent(hl);

        check.addClickListener(clickEvent -> {
            try {
                average.setValue(String.valueOf(nbpApiService.calculateAverage(nbpApiService.parseXmlToList(nbpApiService.getDataByUrl(nbpApiService.createUrl(currency.getValue().toString(), startDate.getValue().toString(), endDate.getValue().toString()))))));
                sd.setValue(String.valueOf(nbpApiService.calculateStandardDeviation(nbpApiService.parseXmlToList(nbpApiService.getDataByUrl(nbpApiService.createUrl(currency.getValue().toString(), startDate.getValue().toString(), endDate.getValue().toString()))))));
                grid.setItems(nbpApiService.parseXmlToList(nbpApiService.getDataByUrl(nbpApiService.createUrl(currency.getValue().toString(), startDate.getValue().toString(), endDate.getValue().toString()))));
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        });

    }
}
