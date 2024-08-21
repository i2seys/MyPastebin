package ru.pastebin.pastebinMicroservice.deserializer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.serializer.DeserializationException;
import ru.pastebin.pastebinMicroservice.dto.PasteRequest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PasteDeserializerTest {
    private static PasteDeserializer pasteDeserializer;
    private static String topic;
    @BeforeAll
    static void init() {
        pasteDeserializer = new PasteDeserializer();
        topic = "";
    }
    @Test
    void deserialize_wrongDataError() {
        assertThrows(DeserializationException.class, () -> pasteDeserializer.deserialize(topic, new byte[]{1}));
    }

    @Test
    void deserialize_nullDataError() {
        assertThrows(DeserializationException.class, () -> pasteDeserializer.deserialize(topic, null));
    }

    @Test
    void deserialize_success() {
        PasteRequest paste = pasteDeserializer.deserialize(
                topic,
                new byte[]{123, 34, 112, 97, 115, 116, 101, 34, 58, 34, 49, 34, 44, 34, 99, 114, 101, 97, 116, 101,
                        95, 116, 105, 109, 101, 34, 58, 49, 55, 50, 52, 48, 55, 54, 50, 57, 55, 49, 52, 55, 125}
        );
        assertEquals(paste, new PasteRequest("1", new Date(1724076297147L)));

        PasteRequest nullPaste = pasteDeserializer.deserialize(
                topic,
                new byte[] {123, 34, 112, 97, 115, 116, 101, 34, 58, 110, 117, 108, 108, 44, 34, 99, 114, 101, 97,
                        116, 101, 95, 116, 105, 109, 101, 34, 58, 110, 117, 108, 108, 125}
        );
        assertEquals(nullPaste, new PasteRequest(null, null));
    }
}
