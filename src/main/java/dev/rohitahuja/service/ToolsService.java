package dev.rohitahuja.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jfree.chart.JFreeChart;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.ai.tool.metadata.ToolMetadata;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Base64;

@Service
public class ToolsService {

    private final ChatClient chatClient;
    private final ChatClient chatClient2;

    public ToolsService(ChatClient.Builder builder, ChatClient.Builder builder2) {
        this.chatClient = builder
                .defaultSystem("""
                You are a helpful AI Assistant that answers based on knowledge available but also utilizes the tools that have been made available.
                """)
                .defaultTools(new ChartService())
                .build();
        this.chatClient2 = builder2
                .defaultSystem("""
                You are a helpful AI Assistant that answers based on knowledge available but also utilizes the tools that have been made available.
                """)
                .build();
    }

    public Byte[] callTool(String message) {
        return chatClient
                .prompt()
                .user(message)
                .call()
                .entity(Byte[].class);
    }

    public BufferedImage callTool2(String message) {
        String content = chatClient2
                .prompt(message)
                .toolCallbacks(FunctionToolCallback
                        .builder("generateBarChart", new ChartFunction())
                        .toolMetadata(ToolMetadata.builder()
                                .returnDirect(true)
                                .build())
                        .toolCallResultConverter(new ChartToolCallResultConverter())
                        .description("Generate bar chart")
                        .inputType(ChartFunction.Request.class)
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
