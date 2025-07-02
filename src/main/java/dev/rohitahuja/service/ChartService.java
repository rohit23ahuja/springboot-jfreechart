package dev.rohitahuja.service;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class ChartService {

    private static final Logger _log = LoggerFactory.getLogger(ChartService.class);

    @Tool(description = "Tool to generate a bar chart")
    public Byte[] generateBarChart(@ToolParam(description = "Title of chart") String title,
                                   @ToolParam(description = "Label for category axis or x-axis in chart") String categoryAxisLabel,
                                   @ToolParam(description = "Label for value axis or y-axis in chart") String valueAxisLabel,
                                   @ToolParam(description = """
        Data that needs to be displayed in chart. 
        First an instance of class type org.jfree.data.category.DefaultCategoryDataset needs to be created.
        Then for each value that needs to displayed in Chart need to call addValue method with arguments - value, rowKey, columnKey
        """) DefaultCategoryDataset dataset) throws Exception {
        _log.info("Generating bar chart.");
        _log.info("Title {} CategoryAxisLabel {} ValueAxisLabel {}", title, categoryAxisLabel, valueAxisLabel);
        _log.info("Dataset values:");
        logDefaultCategoryDataset(dataset);
//        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//        dataset.addValue(10, "Sales", "January");
//        dataset.addValue(15, "Sales", "February");
//        dataset.addValue(20, "Sales", "March");

        JFreeChart chart = ChartFactory.createBarChart(
                title,
                categoryAxisLabel,
                valueAxisLabel,
                dataset
        );
        return buildImageResponse(chart);
    }

//    @Tool(description = "Generates a sample line chart")
//    public JFreeChart getLineChart() {
//        _log.info("Generating line chart...");
//        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//        dataset.addValue(5, "Revenue", "Week 1");
//        dataset.addValue(7, "Revenue", "Week 2");
//        dataset.addValue(6, "Revenue", "Week 3");
//        dataset.addValue(8, "Revenue", "Week 4");
//
//        return ChartFactory.createLineChart(
//                "Weekly Revenue Trend",
//                "Week",
//                "Revenue",
//                dataset
//        );
//    }
//
//    @Tool(description = "Generates a sample pie chart")
//    public JFreeChart getPieChart() {
//        _log.info("Generating pie chart...");
//        DefaultPieDataset dataset = new DefaultPieDataset();
//        dataset.setValue("Product A", 40);
//        dataset.setValue("Product B", 25);
//        dataset.setValue("Product C", 35);
//
//        return ChartFactory.createPieChart(
//                "Product Sales Share",
//                dataset,
//                true, true, false
//        );
//    }

    private Byte[] buildImageResponse(JFreeChart chart) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(baos, chart, 600, 400);
        return toObjectByteArray(baos.toByteArray());
    }

    public static Byte[] toObjectByteArray(byte[] bytes) {
        Byte[] result = new Byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            result[i] = bytes[i];
        }
        return result;
    }

    private void logDefaultCategoryDataset(DefaultCategoryDataset dataset) {
        int rowCount = dataset.getRowCount();
        int colCount = dataset.getColumnCount();
        for (int r = 0; r < rowCount; r++) {
            Comparable<?> rowKey = dataset.getRowKey(r);
            for (int c = 0; c < colCount; c++) {
                Comparable<?> columnKey = dataset.getColumnKey(c);
                Number value = dataset.getValue(r, c);
                _log.info("Dataset value: [rowKey='{}', columnKey='{}'] = {}", rowKey, columnKey, value);
            }
        }
    }


}

