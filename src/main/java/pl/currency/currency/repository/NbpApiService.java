package pl.currency.currency.repository;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import pl.currency.currency.model.Rate;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Lukasz on 28.11.2017.
 */
public class NbpApiService implements INbpApiService {

    //Metoda tworzaca odpowiedni URL na podstawie parametrow
    public String createUrl(String currency, String startDate, String endDate) {

        StringBuilder url;

        url = new StringBuilder();
        url.append("http://api.nbp.pl/api/exchangerates/rates/c/");
        url.append(currency);
        url.append("/");
        url.append(startDate);
        url.append("/");
        url.append(endDate);
        url.append("/?format=xml");

        return url.toString();
    }

    //Metoda pobierajaca XML do stringa z podanego URLA
    public String getDataByUrl(String url) {
        URL urls;
        BufferedReader bf;
        String line;
        InputStream input = null;
        StringBuilder sb = new StringBuilder();

        try {
            urls = new URL(url);
            input = urls.openStream();
            bf = new BufferedReader(new InputStreamReader(input));

            while ((line = bf.readLine()) != null) {
                sb.append(line);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) input.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        return sb.toString();
    }


    //Metoda przetwarzajaca XML na obiekty klasy Rate
    public List<Rate> parseXmlToList(String xml) throws ParserConfigurationException, IOException, SAXException {

        Rate rate;
        List<Rate> rates = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xml)));

        NodeList nodes = document.getElementsByTagName("Rate");

        for (int i = 0; i < nodes.getLength(); i++) {

            Element element = (Element) nodes.item(i);
            rate = new Rate();

            NodeList mid = element.getElementsByTagName("Bid");
            Element line = (Element) mid.item(0);
            rate.setMid(Double.parseDouble(line.getFirstChild().getTextContent()));

            NodeList date = element.getElementsByTagName("EffectiveDate");
            line = (Element) date.item(0);
            rate.setDate(line.getFirstChild().getTextContent());

            rates.add(rate);

        }
        return rates;
    }

    //Metoda liczaca srednia
    public Double calculateAverage(List<Rate> rates) {
        Double average = 0.0;

        for(Rate item: rates) {
            average+=item.getMid();
        }


        return average/rates.size();
    }

    //Metoda liczaca odchylenie standardowe (wynik dla podanego w zadaniu przykladu jest minimalnie inny , z niejasnych mi przyczyn :) )
    public Double calculateStandardDeviation(List<Rate> rates) {
        Double variance = 0.000;
        Double avg = calculateAverage(rates);

        for(Rate item: rates) {
            variance += (item.getMid()-avg) * (item.getMid()-avg);
        }
        variance = variance/(rates.size()-1);
        return Math.sqrt(variance);
    }

}

