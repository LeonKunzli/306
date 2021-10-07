package View;

import Controllers.Reader;
import Model.Messwert;
import com.formdev.flatlaf.FlatIntelliJLaf;
import org.jdesktop.swingx.JXDatePicker;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

/**
 * 306
 *
 * @author Léon Künzli
 * @version 0.18
 * @since 07.10.2021
 */
public class GUI extends JFrame {
    private Reader reader = new Reader();
    private JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "Electricity Monitoring", // Chart title
            "Zeit in Minuten", // X-Axis Label
            "KWH", // Y-Axis Label
            null
    );
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
        JPanel radioButtonPanel = new JPanel(new BorderLayout());
        radioButtonPanel.setBackground(new Color(0xFFFFFF));
        radioButtonPanel.setPreferredSize(new Dimension(620,190));
        JPanel exportPanel = new JPanel(new FlowLayout());
        exportPanel.setPreferredSize(new Dimension(500, 80));
        exportPanel.setBackground(new Color(0xFFFFFF));
        JPanel importPanel = new JPanel(new FlowLayout());
        importPanel.setBackground(new Color(0xFFFFFF));
        importPanel.setPreferredSize(new Dimension(500,80));
        ChartPanel chartPanel = new ChartPanel(chart);
        JButton importESLButton = new JButton("Import ESL");
        JPanel skipPanel = new JPanel(new FlowLayout());
        skipPanel.setBackground(new Color(0xFFFFFF));
        JPanel datePickerPanel = new JPanel(new FlowLayout());
        datePickerPanel.setBackground(new Color(0xFFFFFF));
        JXDatePicker startDatePicker = new JXDatePicker();
        JXDatePicker endDatePicker = new JXDatePicker();
        JButton submitDatePicker = new JButton("Apply");
        submitDatePicker.addActionListener(e ->{
            if(startDatePicker.getDate()!=null || endDatePicker.getDate()!=null) {
                setTime(startDatePicker.getDate().getTime(), endDatePicker.getDate().getTime());
            }
            else{
                JOptionPane.showMessageDialog(null,
                        "Bitte geben wählen Sie ein Datum aus.", "Warnung", JOptionPane.WARNING_MESSAGE);

            }
        });
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
                XYDataset dataset = createDataset(reader.getRichtigeMap(), null);
                chart = ChartFactory.createTimeSeriesChart(
                        "Electricity Monitoring", // Chart title
                        "Zeit in Minuten", // X-Axis Label
                        "KWH", // Y-Axis Label
                        dataset
                );
                chart.setBackgroundPaint(new Color(0xFFFFFF));
                chart.fireChartChanged();
                chartPanel.setChart(chart);
            }

    });
        importESLButton.setPreferredSize(new Dimension(305, 80));
        JButton importSDATButton = new JButton("Import SDAT");
        importSDATButton.addActionListener(e -> {
            if(reader.isHasESL()) {
                JFileChooser chooser = new JFileChooser();
                chooser.setAcceptAllFileFilterUsed(false);
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int dialogReturnValue = chooser.showOpenDialog(null);
                if (dialogReturnValue == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile().getAbsoluteFile();
                    System.out.println(file);
                    reader.readSDAT(file.toString());
                    JOptionPane.showMessageDialog(null,
                            "Imported new Files.");
                    XYDataset dataset = createDataset(reader.getRichtigeMap(), null);
                    chart = ChartFactory.createTimeSeriesChart(
                            "Electricity Monitoring", // Chart title
                            "Zeit in Minuten", // X-Axis Label
                            "KWH", // Y-Axis Label
                            dataset
                    );
                    chart.setBackgroundPaint(new Color(0xFFFFFF));
                    chart.fireChartChanged();
                    chartPanel.setChart(chart);
                }
            }
            else {
                JOptionPane.showMessageDialog(null,
                        "Bitte importieren Sie ESL zuerst.", "Warnung", JOptionPane.WARNING_MESSAGE);
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
                if(reader.isHasSDAT()) {
                    JFileChooser chooser = new JFileChooser();
                    FileNameExtensionFilter jsonFilter = new FileNameExtensionFilter("json files (*.json)", "json");
                    chooser.addChoosableFileFilter(jsonFilter);
                    chooser.setFileFilter(jsonFilter);
                    int dialogReturnValue = chooser.showSaveDialog(null);
                    if(dialogReturnValue==JFileChooser.APPROVE_OPTION) {
                        File file = chooser.getSelectedFile();
                        if (!file.getName().endsWith(".json")) {
                            file = new File(file + ".json");
                        }
                        try {
                            reader.exportJSON(file.toString());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null,
                                    "Etwas ist schief gegangen.", "Warnung", JOptionPane.WARNING_MESSAGE);
                        }
                        JOptionPane.showMessageDialog(null,
                                "saved file as " + file.getAbsoluteFile());
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null,
                            "Bitte fügen Sie ESL und SDAT Daten ein.", "Warnung", JOptionPane.WARNING_MESSAGE);
                }

        });

        exportCSVButton.setPreferredSize(new Dimension(305, 80));
        exportJSONButton.setPreferredSize(new Dimension(305, 80));
        JRadioButton verbrauchsRadioButton = new JRadioButton("Verbrauchsdiagramm");
        verbrauchsRadioButton.setBackground(new Color(0xFFFFFF));
        verbrauchsRadioButton.setPreferredSize(new Dimension(500, 80));
        verbrauchsRadioButton.addActionListener(e -> {
            absoluteZahlen = false;
            XYDataset dataset = createDataset(reader.getRichtigeMap(), null);
            chart = ChartFactory.createTimeSeriesChart(
                    "Electricity Monitoring", // Chart title
                    "Zeit in Minuten", // X-Axis Label
                    "KWH", // Y-Axis Label
                    dataset
            );
            chart.setBackgroundPaint(new Color(0xFFFFFF));
            chart.fireChartChanged();
            chartPanel.setChart(chart);
        });
        JRadioButton zählerRadioButton = new JRadioButton("Zählerdiagramm");
        zählerRadioButton.setBackground(new Color(0xFFFFFF));
        zählerRadioButton.setPreferredSize(new Dimension(500, 80));
        zählerRadioButton.setSelected(true);
        zählerRadioButton.addActionListener(e -> {
            absoluteZahlen = true;
            XYDataset dataset = createDataset(reader.getRichtigeMap(), null);
            chart = ChartFactory.createTimeSeriesChart(
                    "Electricity Monitoring", // Chart title
                    "Zeit in Minuten", // X-Axis Label
                    "KWH", // Y-Axis Label
                    dataset
            );
            chart.setBackgroundPaint(new Color(0xFFFFFF));
            chart.fireChartChanged();
            chartPanel.setChart(chart);
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
        daysWestBasicArrowButton.addActionListener(e -> addTimeOffset(-(24*60*60*1000)));
        BasicArrowButton daysEastBasicArrowButton = new BasicArrowButton(BasicArrowButton.EAST);
        daysEastBasicArrowButton.addActionListener(e -> addTimeOffset(24*60*60*1000));
        BasicArrowButton minuteEastBasicArrowButton = new BasicArrowButton(BasicArrowButton.EAST);
        minuteEastBasicArrowButton.addActionListener(e -> addTimeOffset(15*60*1000));
        BasicArrowButton minuteWestBasicArrowButton = new BasicArrowButton(BasicArrowButton.WEST);
        minuteWestBasicArrowButton.addActionListener(e -> addTimeOffset(-(15*60*1000)));

        JLabel minuteLabel = new JLabel("15 Minuten");
        minuteLabel.setPreferredSize(new Dimension(150, 35));
        JLabel daysLabel = new JLabel("1 Tag");
        daysLabel.setPreferredSize(new Dimension(150, 35));
        ImageIcon icon = new ImageIcon("../../Content/133028-200.png");
        JLabel iconLabel = new JLabel(icon, JLabel.CENTER);


        datePickerPanel.add(startDatePicker);
        datePickerPanel.add(iconLabel);
        datePickerPanel.add(endDatePicker);
        datePickerPanel.add(submitDatePicker);
        exportPanel.add(exportCSVButton);
        exportPanel.add(exportJSONButton);
        minutePanel.add(minuteLabel, BorderLayout.CENTER);
        minutePanel.add(minuteEastBasicArrowButton, BorderLayout.EAST);
        minutePanel.add(minuteWestBasicArrowButton, BorderLayout.WEST);
        daysPanel.add(daysLabel, BorderLayout.CENTER);
        daysPanel.add(daysEastBasicArrowButton, BorderLayout.EAST);
        daysPanel.add(daysWestBasicArrowButton, BorderLayout.WEST);
        skipPanel.add(daysPanel);
        skipPanel.add(minutePanel);
        importPanel.add(importESLButton);
        importPanel.add(importSDATButton);
        importExportPanel.add(importPanel, BorderLayout.NORTH);
        importExportPanel.add(exportPanel, BorderLayout.SOUTH);
        ePanel.add(datePickerPanel, BorderLayout.NORTH);
        ePanel.add(skipPanel, BorderLayout.SOUTH);
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
                s1.addOrUpdate(new Minute(new Date(key)), verbrauchterStrom.get(key).getAbsoluterWert());
            }
            else{
                s1.addOrUpdate(new Minute(new Date(key)), verbrauchterStrom.get(key).getRelativerWert());
            }
        }

        TimeSeries s2 = new TimeSeries("Erzeugter Strom");
        if(erzeugterStrom!=null) {
            for (Long key : erzeugterStrom.keySet()) {
                if (absoluteZahlen) {
                    s2.addOrUpdate(new Minute(new Date(key)), erzeugterStrom.get(key).getAbsoluterWert());
                } else {
                    s2.addOrUpdate(new Minute(new Date(key)), erzeugterStrom.get(key).getRelativerWert());
                }
            }
        }
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        dataset.addSeries(s2);

        return dataset;
    }

    private void addTimeOffset(double offset){
        setTime(chart.getXYPlot().getDomainAxis().getLowerBound()+offset, chart.getXYPlot().getDomainAxis().getUpperBound()+offset);
    }

    private void setTime(double startTime, double endTime){
        chart.getXYPlot().getDomainAxis().setUpperBound(endTime);
        chart.getXYPlot().getDomainAxis().setLowerBound(startTime);
    }
}

