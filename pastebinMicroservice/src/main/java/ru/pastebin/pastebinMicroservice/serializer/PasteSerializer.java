package ru.pastebin.pastebinMicroservice.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;
import ru.pastebin.pastebinMicroservice.dto.Paste;

@Slf4j
@Component
public class PasteSerializer implements Serializer<Paste> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public byte[] serialize(String topic, Paste paste) {
        try {
            if (paste == null){
                log.warn("Null received at serializing");
                return null;
            }
            return objectMapper.writeValueAsBytes(paste);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing Paste to byte[]");
        }
    }
}
