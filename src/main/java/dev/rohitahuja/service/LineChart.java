package dev.rohitahuja.service;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.function.Function;

public class LineChart implements Function<LineChart.Request, BufferedImage> {
    private static final Logger _log = LoggerFactory.getLogger(LineChart.class);

    @Override
    public BufferedImage apply(LineChart.Request request) {
        _log.info("Generating line chart.");
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (LineChart.Item item : request.data()) {
            dataset.addValue(item.value(), item.rowKey(), item.columnKey());
        }

        JFreeChart chart = ChartFactory.createLineChart(
                request.title(),
                request.categoryAxisLabel(),
                request.valueAxisLabel(),
                dataset
        );

        return chart.createBufferedImage(600, 400);
    }

    @JsonClassDescription("Request to generate a line chart")
    public record Request(@JsonProperty(required = true, value = "title")
                          @JsonPropertyDescription("Title of chart") String title,
                          @JsonProperty(required = true, value = "categoryAxisLabel")
                          @JsonPropertyDescription("Label for category axis or x-axis in chart") String categoryAxisLabel,
                          @JsonProperty(required = true, value = "valueAxisLabel")
                          @JsonPropertyDescription("Label for value axis or y-axis in chart") String valueAxisLabel,
                          @JsonProperty(required = true, value = "data")
                          @JsonPropertyDescription("""
                                               List of Data that need to be displayed in chart. 
                                               Format :- 
                                               value – the Number value.
                                               rowKey – the row key in chart .
                                               columnKey – the column key in chart.
                                  """) List<LineChart.Item> data) {
    }

    @JsonClassDescription("Single value in a chart identified by row key and column key")
    public record Item(@JsonProperty(required = true, value = "value")
                       @JsonPropertyDescription("numerical value in chart") Number value,
                       @JsonProperty(required = true, value = "rowKey")
                       @JsonPropertyDescription("row key in chart") String rowKey,
                       @JsonProperty(required = true, value = "columnKey")
                       @JsonPropertyDescription("column key in chart") String columnKey
    ) {
    }

    @JsonClassDescription("Response to get BufferedImage")
    public record Response(@JsonPropertyDescription("bufferedImage") BufferedImage bufferedImage) {
    }
}
