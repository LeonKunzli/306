package View;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class GUI extends JFrame {

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

        JButton importButton = new JButton("import files");
        importButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setAcceptAllFileFilterUsed(false);
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int dialogReturnValue = chooser.showOpenDialog(null);
                if(dialogReturnValue==JFileChooser.APPROVE_OPTION){
                    File file = chooser.getSelectedFile().getAbsoluteFile();
                    JOptionPane.showMessageDialog(null,
                            "Imported " + file.getAbsoluteFile());
                    init();
                }
            }
        });
        importButton.setPreferredSize(new Dimension(500, 80));
        JButton exportCSVButton = new JButton("export CSV");
        JButton exportJSONButton = new JButton("export JSON");
        exportCSVButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("csv files (*.csv)", "csv");
                chooser.addChoosableFileFilter(csvFilter);
                chooser.setFileFilter(csvFilter);
                int dialogReturnValue = chooser.showSaveDialog(null);
                if(dialogReturnValue==JFileChooser.APPROVE_OPTION){
                    File file = chooser.getSelectedFile();
                    if (!file.getName() .endsWith(".csv")){
                        file = new File(file.toString() + ".csv");
                    }
                    System.out.println(file.getAbsoluteFile());
                    JOptionPane.showMessageDialog(null,
                            "saved file as " + file.getAbsoluteFile());
                }
            }
        });
        exportJSONButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter jsonFilter = new FileNameExtensionFilter("json files (*.json)", "json");
                chooser.addChoosableFileFilter(jsonFilter);
                chooser.setFileFilter(jsonFilter);
                int dialogReturnValue = chooser.showSaveDialog(null);
                if(dialogReturnValue==JFileChooser.APPROVE_OPTION){
                    File file = chooser.getSelectedFile();
                    if (!file.getName().endsWith(".json")){
                        file = new File(file.toString() + ".json");
                    }
                    JOptionPane.showMessageDialog(null,
                            "saved file as " + file.getAbsoluteFile());
                }
            }
        });

        exportCSVButton.setPreferredSize(new Dimension(305, 80));
        exportJSONButton.setPreferredSize(new Dimension(305, 80));
        JRadioButton verbrauchsRadioButton = new JRadioButton("Verbrauchsdiagramm");
        verbrauchsRadioButton.setBackground(new Color(0xFFFFFF));
        verbrauchsRadioButton.setPreferredSize(new Dimension(500, 80));
        JRadioButton zählerRadioButton = new JRadioButton("Zählerdiagramm");
        zählerRadioButton.setBackground(new Color(0xFFFFFF));
        zählerRadioButton.setPreferredSize(new Dimension(500, 80));
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

        XYDataset dataset = createDataset();
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
        importExportPanel.add(importButton, BorderLayout.NORTH);
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

    private static XYDataset createDataset() {

        TimeSeries s1 = new TimeSeries("Verbrauchter Strom");
        s1.add(new Month(2, 2001), 181.8);
        s1.add(new Month(3, 2001), 167.3);
        s1.add(new Month(4, 2001), 153.8);
        s1.add(new Month(5, 2001), 167.6);
        s1.add(new Month(6, 2001), 158.8);
        s1.add(new Month(7, 2001), 148.3);
        s1.add(new Month(8, 2001), 153.9);
        s1.add(new Month(9, 2001), 142.7);
        s1.add(new Month(10, 2001), 123.2);
        s1.add(new Month(11, 2001), 131.8);
        s1.add(new Month(12, 2001), 139.6);
        s1.add(new Month(1, 2002), 142.9);
        s1.add(new Month(2, 2002), 138.7);
        s1.add(new Month(3, 2002), 137.3);
        s1.add(new Month(4, 2002), 143.9);
        s1.add(new Month(5, 2002), 139.8);
        s1.add(new Month(6, 2002), 137.0);
        s1.add(new Month(7, 2002), 132.8);

        TimeSeries s2 = new TimeSeries("Erzeugter Strom");
        s2.add(new Month(2, 2001), 129.6);
        s2.add(new Month(3, 2001), 123.2);
        s2.add(new Month(4, 2001), 117.2);
        s2.add(new Month(5, 2001), 124.1);
        s2.add(new Month(6, 2001), 122.6);
        s2.add(new Month(7, 2001), 119.2);
        s2.add(new Month(8, 2001), 116.5);
        s2.add(new Month(9, 2001), 112.7);
        s2.add(new Month(10, 2001), 101.5);
        s2.add(new Month(11, 2001), 106.1);
        s2.add(new Month(12, 2001), 110.3);
        s2.add(new Month(1, 2002), 111.7);
        s2.add(new Month(2, 2002), 111.0);
        s2.add(new Month(3, 2002), 109.6);
        s2.add(new Month(4, 2002), 113.2);
        s2.add(new Month(5, 2002), 111.6);
        s2.add(new Month(6, 2002), 108.8);
        s2.add(new Month(7, 2002), 101.6);

        // ******************************************************************
        //  More than 150 demo applications are included with the JFreeChart
        //  Developer Guide...for more information, see:
        //
        //  >   http://www.object-refinery.com/jfreechart/guide.html
        //
        // ******************************************************************

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        dataset.addSeries(s2);

        return dataset;

    }
}

