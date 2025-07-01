package dev.rohitahuja.service;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;

@Service
public class ChartService {

    private static final Logger _log = LoggerFactory.getLogger(ChartService.class);

    @Tool(description = "Generates a sample bar chart", returnDirect = true)
    public Byte[] getBarChart() throws Exception {
        _log.info("Generating bar chart...");
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(10, "Sales", "January");
        dataset.addValue(15, "Sales", "February");
        dataset.addValue(20, "Sales", "March");

        JFreeChart chart = ChartFactory.createBarChart(
                "Monthly Sales",
                "Month",
                "Amount",
                dataset
        );
        return buildImageResponse(chart);
    }

    @Tool(description = "Generates a sample line chart", returnDirect = true)
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

    @Tool(description = "Generates a sample pie chart", returnDirect = true)
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

    public static Byte[] toObjectByteArray(byte[] bytes) {
        Byte[] result = new Byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            result[i] = bytes[i];
        }
        return result;
    }

}

