package ru.pastebin.pastebinMicroservice.serializer;

import org.apache.kafka.common.errors.SerializationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.pastebin.pastebinMicroservice.dto.PasteRequest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PasteSerializerTest {
    private static PasteSerializer pasteSerializer;
    private static String topic = "";
    @BeforeAll
    static void init() {
        pasteSerializer = new PasteSerializer();
        topic = "";
    }

    @Test
    void serialize_serializeNullRequestError() {
        assertThrows(SerializationException.class, () -> pasteSerializer.serialize(topic, null));
    }

    @Test
    void serialize_serializeSuccess() {
        pasteSerializer.serialize(topic, new PasteRequest());
        pasteSerializer.serialize(topic, new PasteRequest("1", new Date()));
        pasteSerializer.serialize(topic, new PasteRequest("1", new Date(0)));
        pasteSerializer.serialize(topic, new PasteRequest(null, new Date(0)));
        pasteSerializer.serialize(topic, new PasteRequest("1", null));
        pasteSerializer.serialize(topic, new PasteRequest(null, null));
    }
}
