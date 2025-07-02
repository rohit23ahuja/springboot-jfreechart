package dev.rohitahuja.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.ai.tool.metadata.ToolMetadata;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Base64;

@Service
public class ToolsService {

    private final ChatClient chatClient;

    public ToolsService(ChatClient.Builder builder, ChatClient.Builder builder2) {
        this.chatClient = builder2
                .defaultSystem("""
                You are a helpful AI Assistant that answers based on knowledge available but also utilizes the tools that have been made available.
                """)
                .build();
    }

//    public Byte[] callTool(String message) {
//        return chatClient
//                .prompt()
//                .user(message)
//                .call()
//                .entity(Byte[].class);
//    }

    public BufferedImage callTool(String message) {
        String content = chatClient
                .prompt(message)
                .toolCallbacks(FunctionToolCallback
                        .builder("generateBarChart", new BarChart())
                        .toolMetadata(ToolMetadata.builder()
                                .returnDirect(true)
                                .build())
                        //.toolCallResultConverter(new ChartToolCallResultConverter())
                        .description("Generate bar chart")
                        .inputType(BarChart.Request.class)
                        .build(),
                        FunctionToolCallback
                                .builder("generateLineChart", new LineChart())
                                .toolMetadata(ToolMetadata.builder()
                                        .returnDirect(true)
                                        .build())
                                //.toolCallResultConverter(new ChartToolCallResultConverter())
                                .description("Generate line chart")
                                .inputType(LineChart.Request.class)
                                .build(),
                        FunctionToolCallback
                                .builder("generatePieChart", new PieChart())
                                .toolMetadata(ToolMetadata.builder()
                                        .returnDirect(true)
                                        .build())
                                //.toolCallResultConverter(new ChartToolCallResultConverter())
                                .description("Generate pie chart")
                                .inputType(PieChart.Request.class)
                                .build())
                .call()
                .content();
        return decodeBase64ToImage(content);
    }

    public BufferedImage decodeBase64ToImage(String jsonString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            var node = mapper.readTree(jsonString);
            String base64 = node.get("data").asText();
            byte[] imageBytes = Base64.getDecoder().decode(base64);
            try (ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes)) {
                return ImageIO.read(bis);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to decode json image string", ex);
        }
    }

}
