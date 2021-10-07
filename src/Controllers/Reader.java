package Controllers;
import Model.Messwert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 306
 *
 * @author Arbias imeri
 * @version 1.2
 * @since 04.10.2021
 */
public class Reader{
    private boolean hasSDAT = false;
    private boolean hasESL = false;
    private static final int SDAT_STEP_IN_MILLISECONDS = 900000;
    private TreeMap<Integer, Double> absoluteMap;
    private long unixTime;
    private TreeMap<Long, Messwert> richtigeMap;
    private double gesamtZahl;
    private Vector<File> fileVector;
    private final Long totalKonstante1 = 1551394800000L;
    private DocumentBuilderFactory factory;

        //ESL
    public Reader() {
        fileVector = new Vector<>();
        absoluteMap = new TreeMap<>();
        richtigeMap = new TreeMap<>();
        factory = DocumentBuilderFactory.newInstance();
    }
    public void readESL(String path){
        try {
            File file = new File(path);
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(file);
            document.getDocumentElement().normalize();
            NodeList eslList = document.getElementsByTagName("ValueRow");

            NodeList timePeriodESL = document.getElementsByTagName("TimePeriod");
            String endDateTime = timePeriodESL.item(0).getAttributes().item(0).getTextContent();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = dateFormat.parse(endDateTime);
            unixTime = (long) date.getTime();
            System.out.println(unixTime);

            for (int tmp = 0; tmp < eslList.getLength(); tmp++){

                Node bruche = eslList.item(tmp);
                Element observationElement = (Element)bruche;
                if (bruche.getNodeType() == Node.ELEMENT_NODE && observationElement.getAttribute("obis").equals("1-1:1.8.1") ||
                        bruche.getNodeType() == Node.ELEMENT_NODE && observationElement.getAttribute("obis").equals("1-1:1.8.2")){
                    double zahl = Double.parseDouble(observationElement.getAttribute("value"));
                    absoluteMap.put(tmp, zahl);
                }
            }
            gesamtZahl = absoluteMap.get(2) + absoluteMap.get(3);
            richtigeMap.put(unixTime, new Messwert(gesamtZahl, 0.0));


            hasESL = true;
            System.out.println(richtigeMap);

        }catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }



    }
    public void readSDAT(String path){
        //TODO Arbias weiss
        try {
            File folder = new File(path);
            DocumentBuilder builder2 = factory.newDocumentBuilder();

            for (File fileEntry : folder.listFiles()) {
                fileVector.add(fileEntry);
            }

            for (int i = 0; i < fileVector.size(); i++) {
                Document document1 = builder2.parse(fileVector.get(i));
                document1.getDocumentElement().normalize();
                NodeList observationsList = document1.getElementsByTagName("rsm:Observation");
                NodeList timePeriodSDAT = document1.getElementsByTagName("rsm:Interval");
                Node timePeriod = timePeriodSDAT.item(0);
                Element timeElement = (Element) timePeriod;
                String dateS = timeElement.getElementsByTagName("rsm:StartDateTime").item(0).getTextContent();
                DateFormat dateFormatFuerneu = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                Date dateFuerDataS = dateFormatFuerneu.parse(dateS);
                unixTime = dateFuerDataS.getTime();

                for (int j = 0; j < observationsList.getLength(); j++) {
                    Node observation = observationsList.item(j);
                    if (observation.getNodeType() == Node.ELEMENT_NODE) {
                        Element observationElement = (Element) observation;
                        int sequence = Integer.parseInt(observationElement.getElementsByTagName("rsm:Sequence").item(0).getTextContent());
                        double value = Double.parseDouble(observationElement.getElementsByTagName("rsm:Volume").item(0).getTextContent());
                        unixTime += SDAT_STEP_IN_MILLISECONDS;
                        if (richtigeMap.get(totalKonstante1) != null) {
                            richtigeMap.put(unixTime, new Messwert(richtigeMap.get(totalKonstante1).getAbsoluterWert(), value));
                        } else {
                            richtigeMap.put(unixTime, new Messwert(0, value));

                        }
                    }
                }

            }
            double temp = richtigeMap.get(totalKonstante1).getAbsoluterWert();
            for (Map.Entry<Long, Messwert> entry : richtigeMap.entrySet()) {
                long key = entry.getKey();
                Messwert value = entry.getValue();
                if (totalKonstante1 <= key) {
                    double erstezZahl = richtigeMap.get(totalKonstante1).getAbsoluterWert();
                    temp += value.getRelativerWert();
                    value.setAbsoluterWert(temp);
                    System.out.println(value.getAbsoluterWert());
                    System.out.println(value + " AAAAAAAAAAAAAAAAAAAAAA " + erstezZahl);
                }
            }
            hasSDAT = true;
            System.out.println(temp);
        }catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        //System.out.println(richtigeMap);
    }

    public void setHasSDAT(boolean hasSDAT) {
        this.hasSDAT = hasSDAT;
    }

    public void setHasESL(boolean hasESL) {
        this.hasESL = hasESL;
    }

    public boolean isHasSDAT() {
        return hasSDAT;
    }

    public boolean isHasESL() {
        return hasESL;
    }

    public TreeMap<Long, Messwert> getRichtigeMap() {
        return richtigeMap;
    }
}
