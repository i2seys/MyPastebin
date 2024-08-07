package ru.pastebin.pastebinMicroservice.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.stereotype.Component;
import ru.pastebin.pastebinMicroservice.dto.Paste;

import java.io.IOException;


@Component
public class PasteDeserializer implements Deserializer<Paste> {
    private ObjectMapper objectMapper;

    public PasteDeserializer() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Paste deserialize(String topic, byte[] data) {
        try {
            Paste paste = data == null ? null : objectMapper.readValue(data, Paste.class);
            return paste;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
