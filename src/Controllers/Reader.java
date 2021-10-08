package Controllers;/*
package Controllers;
import Messwert;
import com.formdev.flatlaf.json.Json;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

 */


import Model.Messwert;
import com.google.gson.Gson;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private TreeMap<Integer, Double> abosulteMap2;
    private long unixTime;
    private TreeMap<Long, Messwert> erzeugungMap;
    private TreeMap<Long, Messwert> verbrauchMap;
    private double gesamtZahl;
    private Vector<File> fileVector;
    private final Long totalKonstante1 = 1551394800000L;
    private DocumentBuilderFactory factory;
    private String id;


    //ESL
    public Reader() {
        fileVector = new Vector<>();
        absoluteMap = new TreeMap<>();
        abosulteMap2 = new TreeMap<>();
        erzeugungMap = new TreeMap<>();
        verbrauchMap = new TreeMap<>();
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

            for (int tmp = 0; tmp < eslList.getLength(); tmp++){

                Node bruche = eslList.item(tmp);
                Element observationElement = (Element)bruche;
                if (bruche.getNodeType() == Node.ELEMENT_NODE && observationElement.getAttribute("obis").equals("1-1:1.8.1") ||
                        bruche.getNodeType() == Node.ELEMENT_NODE && observationElement.getAttribute("obis").equals("1-1:1.8.2")){
                    double zahl = Double.parseDouble(observationElement.getAttribute("value"));
                    absoluteMap.put(tmp, zahl);
                }else if (bruche.getNodeType() == Node.ELEMENT_NODE && observationElement.getAttribute("obis").equals("1-1:2.8.1") ||
                        bruche.getNodeType() == Node.ELEMENT_NODE && observationElement.getAttribute("obis").equals("1-1:2.8.2")){
                    double zahl = Double.parseDouble(observationElement.getAttribute("value"));
                    abosulteMap2.put(tmp, zahl);
                }
            }
            gesamtZahl = absoluteMap.get(2) + absoluteMap.get(3);
            verbrauchMap.put(unixTime, new Messwert(gesamtZahl, 0.0));

            gesamtZahl = abosulteMap2.get(6) + abosulteMap2.get(7);
            erzeugungMap.put(unixTime, new Messwert(gesamtZahl, 0));


            hasESL = true;

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
        double temp = 0;
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
                NodeList idList = document1.getElementsByTagName("rsm:DocumentID");
                Node timePeriod = timePeriodSDAT.item(0);
                Element timeElement = (Element) timePeriod;
                String dateS = timeElement.getElementsByTagName("rsm:StartDateTime").item(0).getTextContent();
                DateFormat dateFormatFuerneu = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                Date dateFuerDataS = dateFormatFuerneu.parse(dateS);
                unixTime = dateFuerDataS.getTime();

                Node idNode = idList.item(0);
                Element idELement = (Element) idNode;
                id = idELement.getTextContent();


                for (int j = 0; j < observationsList.getLength(); j++) {
                    System.out.println();
                    Node observation = observationsList.item(j);
                    if (observation.getNodeType() == Node.ELEMENT_NODE && id.endsWith("ID735")) {

                        Element observationElement = (Element) observation;
                        int sequence = Integer.parseInt(observationElement.getElementsByTagName("rsm:Sequence").item(0).getTextContent());
                        double value = Double.parseDouble(observationElement.getElementsByTagName("rsm:Volume").item(0).getTextContent());

                        unixTime += SDAT_STEP_IN_MILLISECONDS;
                        if (erzeugungMap.get(totalKonstante1) != null) {
                            erzeugungMap.put(unixTime, new Messwert(erzeugungMap.get(totalKonstante1).getAbsoluterWert(), value));
                        } else {
                            erzeugungMap.put(unixTime, new Messwert(0, value));
                        }
                    }else if (observation.getNodeType() == Node.ELEMENT_NODE && id.endsWith("ID742")){

                        Element observationElement = (Element) observation;
                        int sequence = Integer.parseInt(observationElement.getElementsByTagName("rsm:Sequence").item(0).getTextContent());
                        double value = Double.parseDouble(observationElement.getElementsByTagName("rsm:Volume").item(0).getTextContent());
                        temp+=value;
                        System.out.println(temp);
                        unixTime += SDAT_STEP_IN_MILLISECONDS;
                        if (verbrauchMap.get(totalKonstante1) != null) {
                            verbrauchMap.put(unixTime, new Messwert(verbrauchMap.get(totalKonstante1).getAbsoluterWert(), value));
                        } else {
                            verbrauchMap.put(unixTime, new Messwert(0, value));
                        }
                    }
                }

            }
            double erzeugungsTemp = erzeugungMap.get(totalKonstante1).getAbsoluterWert();
            double verbrauchsTemp = verbrauchMap.get(totalKonstante1).getAbsoluterWert();
            for (Map.Entry<Long, Messwert> entry : erzeugungMap.entrySet()) {
                long key = entry.getKey();
                Messwert value = entry.getValue();
                if (totalKonstante1 <= key) {
                    erzeugungsTemp += value.getRelativerWert();
                    value.setAbsoluterWert(erzeugungsTemp);
                }

            }
            for (Map.Entry<Long, Messwert> entry : verbrauchMap.entrySet()){
                long key = entry.getKey();
                Messwert value = entry.getValue();
                if (totalKonstante1 <= key) {
                    verbrauchsTemp += value.getRelativerWert();
                    value.setAbsoluterWert(verbrauchsTemp);
                }
            }
            hasSDAT = true;

        }catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }



    }


    public void exportJSON(String path) throws IOException {
        Gson gson = new Gson();
        FileWriter fileWriter = new FileWriter(path);
        fileWriter.write(gson.toJson(gson.toJson(erzeugungMap)));
    }

    public TreeMap<Long, Messwert> getVerbrauchMap() {
        return verbrauchMap;
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

    public void exportCsv(String path){

    }
}