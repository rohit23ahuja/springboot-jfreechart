package dev.rohitahuja.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import dev.rohitahuja.service.ToolsService;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@PageTitle("Charts Chat")
@Route(value = "", layout = MainLayout.class)
public class ChartView extends VerticalLayout {

    public ChartView(ToolsService toolsService) {
        var chartList = new VerticalLayout();
        var scroller = new Scroller(chartList);
        var messageInput = new MessageInput();
        messageInput.setWidthFull();
        scroller.setWidthFull();
        chartList.setWidthFull();

        messageInput.addSubmitListener(e -> {
            String userMessage = e.getValue();
            // Add user message as label or any component (you may use a custom one)
            chartList.add(new Div("You: " + userMessage));

            try {
                Byte[] bytes = toolsService.callTool(userMessage);
                byte[] primitiveBytes = toPrimitiveByteArray(bytes);
                StreamResource resource = new StreamResource("chart.png", () -> new ByteArrayInputStream(primitiveBytes));
                Image chartImage = new Image(resource, "Generated Chart");
                chartImage.setMaxWidth("500px");
                chartImage.setMaxHeight("400px");
                chartList.add(chartImage);
            } catch (Exception ex) {
                chartList.add(new Div("Error: " + ex.getMessage()));
            }

        });

        addAndExpand(scroller);
        add(messageInput);
    }

    public static byte[] toPrimitiveByteArray(Byte[] bytes) {
        byte[] result = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            result[i] = bytes[i]; // auto-unboxing, beware of possible NullPointerException if bytes[i] is null
        }
        return result;
    }


    public StreamResource createChartResource(BufferedImage image, String filename) {
        return new StreamResource(filename, () -> {
            ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
            try {
                javax.imageio.ImageIO.write(image, "png", imageStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return new ByteArrayInputStream(imageStream.toByteArray());
        });
    }


    public StreamResource createChartResource(JFreeChart chart, String filename) {
        return new StreamResource(filename, () -> {
            ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
            try {
                ChartUtils.writeChartAsPNG(imageStream, chart, 600, 400);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return new ByteArrayInputStream(imageStream.toByteArray());
        });
    }


}
