package ru.pastebin.pastebinMicroservice.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;
import ru.pastebin.pastebinMicroservice.dto.PasteRequest;

@Slf4j
@Component
public class PasteSerializer implements Serializer<PasteRequest> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public byte[] serialize(String topic, PasteRequest pasteRequest) {
        try {
            if (pasteRequest == null){
                throw new SerializationException("Error when serializing PasteRequest(null) to byte[]");
            }
            return objectMapper.writeValueAsBytes(pasteRequest);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing PasteRequest to byte[]");
        }
    }
}
