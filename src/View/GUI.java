package View;

import Controllers.Reader;
import Model.Messwert;
import com.formdev.flatlaf.FlatIntelliJLaf;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.util.StringUtils;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TreeMap;

public class GUI extends JFrame {
    private Reader reader = new Reader();
    public static TreeMap<Long, Messwert> values;
    private boolean absoluteZahlen = true;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI example = new GUI();
            example.pack();
            example.setSize(1920, 1000);
            example.setResizable(false);
            example.setDefaultCloseOperation(example.EXIT_ON_CLOSE);
            example.setVisible(true);
            try {
                UIManager.setLookAndFeel(new FlatIntelliJLaf());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public GUI() throws HeadlessException {
        super();
        init();
    }

    public void init(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0xFFFFFF));
        JPanel nPanel = new JPanel(new FlowLayout());
        nPanel.setBackground(new Color(0xFFFFFF));
        nPanel.setPreferredSize(new Dimension(1920, 190));
        JPanel minutePanel = new JPanel(new BorderLayout());
        JPanel daysPanel = new JPanel(new BorderLayout());
        JPanel importExportPanel = new JPanel(new BorderLayout());
        importExportPanel.setBackground(new Color(0xFFFFFF));
        importExportPanel.setPreferredSize(new Dimension(620, 190));
        JPanel ePanel = new JPanel(new BorderLayout());
        ePanel.setBackground(new Color(0xFFFFFF));
        ePanel.setPreferredSize(new Dimension(620, 190));
        JPanel timeButtonPanel = new JPanel(new FlowLayout());
        timeButtonPanel.setBackground(new Color(0xFFFFFF));
        timeButtonPanel.setPreferredSize(new Dimension(500, 80));
        JPanel datePanel = new JPanel(new FlowLayout());
        datePanel.setBackground(new Color(0xFFFFFF));
        datePanel.setPreferredSize(new Dimension(500, 80));
        JPanel radioButtonPanel = new JPanel(new BorderLayout());
        radioButtonPanel.setBackground(new Color(0xFFFFFF));
        radioButtonPanel.setPreferredSize(new Dimension(620,190));
        JPanel exportPanel = new JPanel(new FlowLayout());
        exportPanel.setPreferredSize(new Dimension(500, 80));
        exportPanel.setBackground(new Color(0xFFFFFF));
        JPanel importPanel = new JPanel(new FlowLayout());
        importPanel.setBackground(new Color(0xFFFFFF));
        importPanel.setPreferredSize(new Dimension(500,80));

        JButton importESLButton = new JButton("Import ESL");
        importESLButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("XML FILES", ".xml", "xml");
            chooser.setFileFilter(filter);

            int dialogReturnValue = chooser.showOpenDialog(null);
            if(dialogReturnValue==JFileChooser.APPROVE_OPTION){
                File file = chooser.getSelectedFile().getAbsoluteFile();
                System.out.println(file);
                reader.readESL(file.toString());
                JOptionPane.showMessageDialog(null,
                        "Imported new Files.");
            }
        });
        importESLButton.setPreferredSize(new Dimension(305, 80));

        JButton importSDATButton = new JButton("Import SDAT");
        importSDATButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int dialogReturnValue = chooser.showOpenDialog(null);
            if(dialogReturnValue==JFileChooser.APPROVE_OPTION){
                File file = chooser.getSelectedFile().getAbsoluteFile();
                System.out.println(file);
                reader.readSDAT(file.toString());
                JOptionPane.showMessageDialog(null,
                        "Imported new Files.");
            }
        });
        importSDATButton.setPreferredSize(new Dimension(305, 80));
        JButton exportCSVButton = new JButton("export CSV");
        JButton exportJSONButton = new JButton("export JSON");
        exportCSVButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("csv files (*.csv)", "csv");
            chooser.addChoosableFileFilter(csvFilter);
            chooser.setFileFilter(csvFilter);
            int dialogReturnValue = chooser.showSaveDialog(null);
            if(dialogReturnValue==JFileChooser.APPROVE_OPTION){
                File file = chooser.getSelectedFile();
                if (!file.getName() .endsWith(".csv")){
                    file = new File(file + ".csv");
                }
                System.out.println(file.getAbsoluteFile());
                JOptionPane.showMessageDialog(null,
                        "saved file as " + file.getAbsoluteFile());
            }
        });
        exportJSONButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter jsonFilter = new FileNameExtensionFilter("json files (*.json)", "json");
            chooser.addChoosableFileFilter(jsonFilter);
            chooser.setFileFilter(jsonFilter);
            int dialogReturnValue = chooser.showSaveDialog(null);
            if(dialogReturnValue==JFileChooser.APPROVE_OPTION){
                File file = chooser.getSelectedFile();
                if (!file.getName().endsWith(".json")){
                    file = new File(file + ".json");
                }
                JOptionPane.showMessageDialog(null,
                        "saved file as " + file.getAbsoluteFile());
            }
        });

        exportCSVButton.setPreferredSize(new Dimension(305, 80));
        exportJSONButton.setPreferredSize(new Dimension(305, 80));
        JRadioButton verbrauchsRadioButton = new JRadioButton("Verbrauchsdiagramm");
        verbrauchsRadioButton.setBackground(new Color(0xFFFFFF));
        verbrauchsRadioButton.setPreferredSize(new Dimension(500, 80));
        verbrauchsRadioButton.addActionListener(e -> {
            absoluteZahlen = false;
            System.out.println(absoluteZahlen);
        });
        JRadioButton zählerRadioButton = new JRadioButton("Zählerdiagramm");
        zählerRadioButton.setBackground(new Color(0xFFFFFF));
        zählerRadioButton.setPreferredSize(new Dimension(500, 80));
        zählerRadioButton.setSelected(true);
        zählerRadioButton.addActionListener(e -> {
            absoluteZahlen = true;
            System.out.println(absoluteZahlen);
        });
        radioButtonPanel.add(verbrauchsRadioButton, BorderLayout.NORTH);
        radioButtonPanel.add(zählerRadioButton, BorderLayout.SOUTH);
        ButtonGroup radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(verbrauchsRadioButton);
        radioButtonGroup.add(zählerRadioButton);

        DateFormat dateFormat = new SimpleDateFormat("^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$");
        JFormattedTextField startDateTextField = new JFormattedTextField(dateFormat);
        startDateTextField.setPreferredSize(new Dimension(200, 50));
        JFormattedTextField endDateTextField = new JFormattedTextField(dateFormat);
        endDateTextField.setPreferredSize(new Dimension(200, 50));

        BasicArrowButton daysWestBasicArrowButton = new BasicArrowButton(BasicArrowButton.WEST);
        daysWestBasicArrowButton.setPreferredSize(new Dimension(100, 350));
        BasicArrowButton daysEastBasicArrowButton = new BasicArrowButton(BasicArrowButton.EAST);
        BasicArrowButton minuteEastBasicArrowButton = new BasicArrowButton(BasicArrowButton.EAST);
        BasicArrowButton minuteWestBasicArrowButton = new BasicArrowButton(BasicArrowButton.WEST);

        XYDataset dataset = null;//createDataset();
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Electricity Monitoring", // Chart title
                "Zeit in Minuten", // X-Axis Label
                "KWH", // Y-Axis Label
                dataset
        );
        JLabel minuteLabel = new JLabel("15 Minuten");
        minuteLabel.setPreferredSize(new Dimension(300, 35));
        JLabel daysLabel = new JLabel("1 Tag");
        daysLabel.setPreferredSize(new Dimension(300, 35));
        ChartPanel chartPanel = new ChartPanel(chart);
        ImageIcon icon = new ImageIcon("../../Content/133028-200.png");
        JLabel iconLabel = new JLabel(icon, JLabel.CENTER);

        exportPanel.add(exportCSVButton);
        exportPanel.add(exportJSONButton);
        minutePanel.add(minuteLabel, BorderLayout.CENTER);
        minutePanel.add(minuteEastBasicArrowButton, BorderLayout.EAST);
        minutePanel.add(minuteWestBasicArrowButton, BorderLayout.WEST);
        daysPanel.add(daysLabel, BorderLayout.CENTER);
        daysPanel.add(daysEastBasicArrowButton, BorderLayout.EAST);
        daysPanel.add(daysWestBasicArrowButton, BorderLayout.WEST);
        importPanel.add(importESLButton);
        importPanel.add(importSDATButton);
        importExportPanel.add(importPanel, BorderLayout.NORTH);
        importExportPanel.add(exportPanel, BorderLayout.SOUTH);
        datePanel.add(startDateTextField);
        datePanel.add(iconLabel);
        datePanel.add(endDateTextField);
        timeButtonPanel.add(minutePanel);
        timeButtonPanel.add(daysPanel);
        ePanel.add(datePanel, BorderLayout.NORTH);
        ePanel.add(timeButtonPanel, BorderLayout.SOUTH);
        nPanel.add(importExportPanel);
        nPanel.add(radioButtonPanel);
        nPanel.add(ePanel);

        panel.add(nPanel, BorderLayout.NORTH);
        panel.add(chartPanel, BorderLayout.SOUTH);

        setContentPane(panel);
    }

    private XYDataset createDataset(TreeMap<Long, Messwert> verbrauchterStrom, TreeMap<Long, Messwert> erzeugterStrom) {

        TimeSeries s1 = new TimeSeries("Verbrauchter Strom");
        for (Long key : verbrauchterStrom.keySet()) {
            if(absoluteZahlen) {
                s1.add(new Minute(new Date(key)), verbrauchterStrom.get(key).getAbsoluterWert());
            }
            else{
                s1.add(new Minute(new Date(key)), verbrauchterStrom.get(key).getRelativerWert());
            }
        }

        TimeSeries s2 = new TimeSeries("Erzeugter Strom");
        for (Long key : erzeugterStrom.keySet()) {
            if (absoluteZahlen){
                s2.add(new Minute(new Date(key)), erzeugterStrom.get(key).getAbsoluterWert());
            }
            else{
                s2.add(new Minute(new Date(key)), erzeugterStrom.get(key).getRelativerWert());
            }
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        dataset.addSeries(s2);

        return dataset;

    }

   /*
    public void readFoldersRecursively(File folder){
        Reader reader = new Reader();
        File[] contents = new File(folder.getAbsolutePath()).listFiles();
        for (File content : contents) {
            System.out.println(content);
            if (content.isFile()){
                System.out.println("is file");
                if(content.toString().endsWith(".xml"))
                    reader.readESL(content.toString());
            }
            else if(content.isDirectory()){
                System.out.println("is directory");
                if(content.getName().toLowerCase().contains("sdat")){
                       reader.readSDAT(content.toString());
                }
                else {
                    readFoldersRecursively(content);
                    System.out.println("YEP");
                }
            }
        }
    } */
}

