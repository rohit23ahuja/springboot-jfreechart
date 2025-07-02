package dev.rohitahuja.service;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.function.Function;

public class PieChart  implements Function<PieChart.Request, BufferedImage> {
    private static final Logger _log = LoggerFactory.getLogger(PieChart.class);

    @Override
    public BufferedImage apply(PieChart.Request request)  {
        _log.info("Generating pie chart.");

        DefaultPieDataset dataset = new DefaultPieDataset();
        for (PieChart.Item item : request.data()) {
            dataset.setValue(item.key(), item.value());
        }
        ChartUtils.logDefaultPieDataset(dataset);
        JFreeChart chart = ChartFactory.createPieChart(
                request.title,
                dataset,
                true, true, false
        );
        return chart.createBufferedImage(600, 400);

    }

    @JsonClassDescription("Request to generate a pie chart")
    public record Request(@JsonProperty(required = true, value = "title")
                          @JsonPropertyDescription("Title of chart") String title,
                          @JsonProperty(required = true, value = "data")
                          @JsonPropertyDescription("""
                                               List of Data that need to be displayed in chart. 
                                               Format :- 
                                               value – the Number value.
                                               key – the key in chart.
                                  """) List<PieChart.Item> data) {
    }

    @JsonClassDescription("Single value in a chart identified by row key and column key")
    public record Item(@JsonProperty(required = true, value = "value")
                       @JsonPropertyDescription("numerical value in chart") Number value,
                       @JsonProperty(required = true, value = "key")
                       @JsonPropertyDescription("key in chart") String key
    ) {
    }

    @JsonClassDescription("Response to get BufferedImage")
    public record Response(@JsonPropertyDescription("bufferedImage") BufferedImage bufferedImage) {
    }
}
