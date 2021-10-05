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
import java.util.Date;
import java.util.TreeMap;
import java.util.Vector;

/**
 * 306
 *
 * @author Arbias imeri
 * @version 1.2
 * @since 04.10.2021
 */
public class Reader{
    private TreeMap<Integer, Double> absoluteMap;
    private long unixTime;
    private TreeMap<Long, Messwert> richtigeMap;
    private double gesamtZahl;
    private Vector<File> fileVector;

/*
    private File file;
    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private Document document;
    private Document document2;
    private NodeList timePeriodList;
    private String  endDateTime;
    private Time time;
    private NodeList observationsList;
    private TreeMap<Long, Messwerte> treeMap;
    private long unixtime;
    private Date date;
    double [] doubles;
    private NodeList ume;

 */

    //ESL
    Reader() {
        fileVector = new Vector<>();
        absoluteMap = new TreeMap<>();
        richtigeMap = new TreeMap<>();
    }
    public TreeMap<Long, Messwert> read(File file){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
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




            File folder = new File("files/XMLfiles/SDAT-Files");
            DocumentBuilder builder2 = factory.newDocumentBuilder();



            for (File fileEntry : folder.listFiles()){
                fileVector.add(fileEntry);
            }
            for (int i = 0; i < fileVector.size(); i++) {




                Document document1 = builder2.parse(fileVector.get(i));
                document1.getDocumentElement().normalize();
                NodeList observationsList = document1.getElementsByTagName("rsm:Observation");
                NodeList timePeriodSDAT = document1.getElementsByTagName("rsm:Interval");
                for (int j = 0; j < observationsList.getLength(); j++) {
                    Node observation = observationsList.item(j);
                    if (observation.getNodeType() == Node.ELEMENT_NODE) {
                        Element observationElement = (Element) observation;
                        int sequence = Integer.parseInt(observationElement.getElementsByTagName("rsm:Sequence").item(0).getTextContent());
                        double value = Double.parseDouble(observationElement.getElementsByTagName("rsm:Volume").item(0).getTextContent());


                    }
                }
            }

        }catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }



        return richtigeMap;
    }
}

