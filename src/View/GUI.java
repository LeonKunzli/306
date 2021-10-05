package View;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.chrono.JapaneseDate;

public class GUI extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI example = new GUI();
            example.setAlwaysOnTop(true);
            example.pack();
            example.setSize(600, 400);
            example.setDefaultCloseOperation(example.EXIT_ON_CLOSE);
            example.setVisible(true);
        });
    }

    public GUI() throws HeadlessException {
        super();
        init();
    }

    public void init(){
        JPanel panel = new JPanel(new BorderLayout());
        JPanel nPanel = new JPanel(new FlowLayout());
        JPanel minutePanel = new JPanel(new BorderLayout());
        JPanel daysPanel = new JPanel(new BorderLayout());
        JPanel importExportPanel = new JPanel();
        JPanel ePanel = new JPanel();
        JPanel timeButtonPanel = new JPanel(new FlowLayout());
        JPanel datePanel = new JPanel(new FlowLayout());

        ePanel.setLayout(new BoxLayout(ePanel, BoxLayout.Y_AXIS));
        ePanel.setLayout(new BoxLayout(importExportPanel, BoxLayout.Y_AXIS));

        JButton importButton = new JButton("import files");
        JButton exportButton = new JButton("export CSV");
        JRadioButton verbrauchsRadioButton = new JRadioButton("Verbrauchsdiagramm");
        JRadioButton zählerRadioButton = new JRadioButton("Zählerdiagramm");
        Box radioButtonBox = Box.createVerticalBox();
        radioButtonBox.add(verbrauchsRadioButton);
        radioButtonBox.add(zählerRadioButton);
        DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
        JFormattedTextField startDateTextField = new JFormattedTextField(dateFormat);
        JFormattedTextField endDateTextField = new JFormattedTextField(dateFormat);
        BasicArrowButton daysWestBasicArrowButton = new BasicArrowButton(BasicArrowButton.WEST);
        BasicArrowButton daysEastBasicArrowButton = new BasicArrowButton(BasicArrowButton.EAST);
        BasicArrowButton minuteEastBasicArrowButton = new BasicArrowButton(BasicArrowButton.EAST);
        BasicArrowButton minuteWestBasicArrowButton = new BasicArrowButton(BasicArrowButton.WEST);
        DefaultCategoryDataset dataset = createDataset();
        JFreeChart chart = ChartFactory.createLineChart(
                "Electricity Monitoring", // Chart title
                "Zeit in Minuten", // X-Axis Label
                "KWH", // Y-Axis Label
                dataset
        );
        JLabel minuteLabel = new JLabel("15 Minuten");
        JLabel daysLabel = new JLabel("1 Tag");
        ChartPanel chartPanel = new ChartPanel(chart);
        ImageIcon icon = new ImageIcon();

        minutePanel.add(minuteLabel, BorderLayout.CENTER);
        minutePanel.add(minuteEastBasicArrowButton, BorderLayout.EAST);
        minutePanel.add(minuteWestBasicArrowButton, BorderLayout.WEST);
        daysPanel.add(daysLabel, BorderLayout.CENTER);
        daysPanel.add(daysEastBasicArrowButton, BorderLayout.EAST);
        daysPanel.add(daysWestBasicArrowButton, BorderLayout.WEST);
        importExportPanel.add(importButton);
        importExportPanel.add(exportButton);
        datePanel.add(startDateTextField);
        datePanel.add(endDateTextField);
        timeButtonPanel.add(startDateTextField);
        timeButtonPanel.add();
        timeButtonPanel.add(endDateTextField);
        ePanel.add(datePanel);
        ePanel.add(timeButtonPanel);
        nPanel.add(importExportPanel);
        nPanel.add(radioButtonBox);
        nPanel.add(ePanel);

        panel.add(nPanel, BorderLayout.NORTH);
        panel.add(chartPanel, BorderLayout.SOUTH);

        setContentPane(panel);
    }

    private DefaultCategoryDataset createDataset() {

        String series1 = "Stromverbrauch";
        String series2 = "Stromerzeugung";

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(200, series1, "2016-12-19");
        dataset.addValue(150, series1, "2016-12-20");
        dataset.addValue(100, series1, "2016-12-21");
        dataset.addValue(210, series1, "2016-12-22");
        dataset.addValue(240, series1, "2016-12-23");
        dataset.addValue(195, series1, "2016-12-24");
        dataset.addValue(245, series1, "2016-12-25");

        dataset.addValue(150, series2, "2016-12-19");
        dataset.addValue(130, series2, "2016-12-20");
        dataset.addValue(95, series2, "2016-12-21");
        dataset.addValue(195, series2, "2016-12-22");
        dataset.addValue(200, series2, "2016-12-23");
        dataset.addValue(180, series2, "2016-12-24");
        dataset.addValue(230, series2, "2016-12-25");

        return dataset;
    }

}