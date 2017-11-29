package pl.currency.currency.repository;

import org.xml.sax.SAXException;
import pl.currency.currency.model.Rate;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

/**
 * Created by Lukasz on 28.11.2017.
 */
//interfejs z sygnaturami metod
public interface INbpApiService {
    String createUrl(String currency,String startDate,String endDate);
    String getDataByUrl(String url);
    List<Rate> parseXmlToList(String xml) throws JAXBException, ParserConfigurationException, IOException, SAXException;
    Double calculateAverage(List<Rate> rates);
    Double calculateStandardDeviation(List<Rate> rates);
}
