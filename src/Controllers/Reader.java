package Controllers;
import Model.Messwert;
import com.formdev.flatlaf.json.Json;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.w3c.dom.*;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
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
    private TreeMap<Long, Messwert> erzeugungMap;
    private double gesamtZahl;
    private Vector<File> fileVector;
    private final Long totalKonstante1 = 1551394800000L;
    private DocumentBuilderFactory factory;
    private TreeMap<Long, Messwert> verbrauchMap;

        //ESL
    public Reader() {
        fileVector = new Vector<>();
        absoluteMap = new TreeMap<>();
        erzeugungMap = new TreeMap<>();
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
            erzeugungMap.put(unixTime, new Messwert(gesamtZahl, 0.0));


            hasESL = true;
            System.out.println(erzeugungMap);

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
                        if (erzeugungMap.get(totalKonstante1) != null) {
                            erzeugungMap.put(unixTime, new Messwert(erzeugungMap.get(totalKonstante1).getAbsoluterWert(), value));
                        } else {
                            erzeugungMap.put(unixTime, new Messwert(0, value));

                        }
                    }
                }

            }
            double temp = erzeugungMap.get(totalKonstante1).getAbsoluterWert();
            for (Map.Entry<Long, Messwert> entry : erzeugungMap.entrySet()) {
                long key = entry.getKey();
                Messwert value = entry.getValue();
                if (totalKonstante1 <= key) {
                    double erstezZahl = erzeugungMap.get(totalKonstante1).getAbsoluterWert();
                    temp += value.getRelativerWert();
                    value.setAbsoluterWert(temp);
                    System.out.println(value.getAbsoluterWert());
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
        //System.out.println(erzeugungMap);
    }

    public void exportJSON(String path) throws IOException {
        Gson gson = new Gson();
        FileWriter fileWriter = new FileWriter(path);
        fileWriter.write(gson.toJson(gson.toJson(erzeugungMap)));
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

    public TreeMap<Long, Messwert> getErzeugungMap() {
        return erzeugungMap;
    }
}
