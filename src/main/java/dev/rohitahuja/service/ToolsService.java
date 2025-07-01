package dev.rohitahuja.service;

import org.jfree.chart.JFreeChart;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.awt.image.BufferedImage;

@Service
public class ToolsService {

    private final ChatClient chatClient;

    public ToolsService(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("You are a helpful AI Assistant that answers based on knowledge available but also utilizes the tools that have been made available.")
                .defaultTools(new ChartService())
                .build();
    }

    public Byte[] callTool(String message) {
        return chatClient
                .prompt()
                .user(message)
                .call()
                .entity(Byte[].class);
    }
}
