package dev.rohitahuja.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.execution.DefaultToolCallResultConverter;
import org.springframework.ai.tool.execution.ToolCallResultConverter;
import org.springframework.ai.util.json.JsonParser;
import org.springframework.lang.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Base64;
import java.util.Map;

public final class ChartToolCallResultConverter  implements ToolCallResultConverter {
    private static final Logger logger = LoggerFactory.getLogger(ChartToolCallResultConverter.class);

    @Override
    public String convert(@Nullable Object result, @Nullable Type returnType) {
        if (returnType == Void.TYPE) {
            logger.debug("The tool has no return type. Converting to conventional response.");
            return JsonParser.toJson("Done");
        }
        if (result instanceof BufferedImage) {
            final var buf = new ByteArrayOutputStream(1024 * 4);
            try {
                ImageIO.write((RenderedImage) result, "PNG", buf);
            }
            catch (IOException e) {
                return "Failed to convert tool result to a base64 image: " + e.getMessage();
            }
            final var imgB64 = Base64.getEncoder().encodeToString(buf.toByteArray());
            return JsonParser.toJson(Map.of("mimeType", "image/png", "data", imgB64));
        }
        else {
            logger.debug("Converting tool result to JSON.");
            return JsonParser.toJson(result);
        }
    }
}
