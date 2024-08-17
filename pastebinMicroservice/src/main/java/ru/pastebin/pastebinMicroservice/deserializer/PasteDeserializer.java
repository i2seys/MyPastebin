package ru.pastebin.pastebinMicroservice.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.stereotype.Component;
import ru.pastebin.pastebinMicroservice.dto.PasteRequest;

import java.io.IOException;


@Component
public class PasteDeserializer implements Deserializer<PasteRequest> {
    private final ObjectMapper objectMapper;

    public PasteDeserializer() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public PasteRequest deserialize(String topic, byte[] data) {
        try {
            return data == null ? null : objectMapper.readValue(data, PasteRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
