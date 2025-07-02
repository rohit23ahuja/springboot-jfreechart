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
import java.util.List;

@Service
public class ChartService {

    public record ChartRequest(
            @ToolParam(description = "Title of chart")
            String title,
            @ToolParam(description = "Label for category axis or x-axis in chart")
            String categoryAxisLabel,
            @ToolParam(description = "Label for value axis or y-axis in chart")
            String valueAxisLabel,
            @ToolParam(description = """
                                           List of Data that need to be displayed in chart. 
                                           Format :- 
                                           value – the Number value.
                                           rowKey – the row key in chart .
                                           columnKey – the column key in chart.
                                           """)
            List<ChartItem> data // each ChartItem holds value, rowKey, columnKey
    ) {}

    public record ChartItem(
            @ToolParam(description = "value in chart")
            Number value,
            @ToolParam(description = "row key in chart")
            String rowKey,
            @ToolParam(description = "column key in chart")
            String columnKey
    ) {}


    private static final Logger _log = LoggerFactory.getLogger(ChartService.class);

    public static Byte[] toObjectByteArray(byte[] bytes) {
        Byte[] result = new Byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            result[i] = bytes[i];
        }
        return result;
    }

    //@Tool(description = "Tool to generate a bar chart")
    public Byte[] generateBarChart(ChartRequest request) throws Exception {
        _log.info("Generating bar chart.");
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (ChartItem item : request.data()) {
            dataset.addValue(item.value(), item.rowKey(), item.columnKey());
        }
        logDefaultCategoryDataset(dataset);
//        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//        dataset.addValue(10, "Sales", "January");
//        dataset.addValue(15, "Sales", "February");
//        dataset.addValue(20, "Sales", "March");

        JFreeChart chart = ChartFactory.createBarChart(
                request.title(),
                request.categoryAxisLabel(),
                request.valueAxisLabel(),
                dataset
        );
        return buildImageResponse(chart);
    }

    //@Tool(description = "Generates a sample line chart")
    public JFreeChart getLineChart() {
        _log.info("Generating line chart...");
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(5, "Revenue", "Week 1");
        dataset.addValue(7, "Revenue", "Week 2");
        dataset.addValue(6, "Revenue", "Week 3");
        dataset.addValue(8, "Revenue", "Week 4");

        return ChartFactory.createLineChart(
                "Weekly Revenue Trend",
                "Week",
                "Revenue",
                dataset
        );
    }

    //@Tool(description = "Generates a sample pie chart")
    public JFreeChart getPieChart() {
        _log.info("Generating pie chart...");
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Product A", 40);
        dataset.setValue("Product B", 25);
        dataset.setValue("Product C", 35);

        return ChartFactory.createPieChart(
                "Product Sales Share",
                dataset,
                true, true, false
        );
    }



    private Byte[] buildImageResponse(JFreeChart chart) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(baos, chart, 600, 400);
        return toObjectByteArray(baos.toByteArray());
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

