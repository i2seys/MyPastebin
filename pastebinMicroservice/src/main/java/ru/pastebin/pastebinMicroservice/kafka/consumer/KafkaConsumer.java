package ru.pastebin.pastebinMicroservice.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.pastebin.pastebinMicroservice.component.FuturePasteComponent;
import ru.pastebin.pastebinMicroservice.deserializer.PasteDeserializer;
import ru.pastebin.pastebinMicroservice.dto.PasteRequest;
import ru.pastebin.pastebinMicroservice.kafka.KafkaTopic;
import ru.pastebin.pastebinMicroservice.model.PasteEntity;
import ru.pastebin.pastebinMicroservice.service.PasteDbService;

import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Slf4j
public class KafkaConsumer {
    private final String groupId = "group-1";

    private final String getGeneratedHashTopicName = "generated-hash-topic";

    private final KafkaTopic kafkaTopic;

    private final PasteDbService pasteDbService;
    private final PasteDeserializer pasteDeserializer;
    private final FuturePasteComponent futurePasteComponent;

    @Autowired
    public KafkaConsumer(
            KafkaTopic kafkaTopic,
            PasteDeserializer pasteDeserializer,
            PasteDbService pasteDbService,
            FuturePasteComponent futurePasteComponent
    ) {
        this.kafkaTopic = kafkaTopic;
        this.pasteDeserializer = pasteDeserializer;
        this.pasteDbService = pasteDbService;
        this.futurePasteComponent = futurePasteComponent;
    }

    @KafkaListener(topics = getGeneratedHashTopicName, groupId = groupId, containerFactory = "getGeneratedHashContainerFactory")
    private void getGeneratedHashTopicListener(ConsumerRecord<Void, String> record) {
        String hash = record.value();

        Headers headers = record.headers();
        Optional<byte[]> pasteBytesOpt = getHeader(headers, "paste");
        Optional<byte[]> requestIdBytesOpt = getHeader(headers, "request-id");
        AtomicReference<PasteRequest> paste = new AtomicReference<>();
        AtomicReference<String> requestId = new AtomicReference<>();

        AtomicBoolean returnBecauseOfHeaderNotPresent = new AtomicBoolean(false);
        pasteBytesOpt.ifPresentOrElse(
                (pasteBytes) -> paste.set(pasteDeserializer.deserialize(kafkaTopic.generateHashTopic().name(), pasteBytes)),
                () -> {
                    log.warn("Paste not present in header");
                    returnBecauseOfHeaderNotPresent.set(true);
                }
        );
        requestIdBytesOpt.ifPresentOrElse(
                (requestIdBytes) -> {
                    try (StringDeserializer s = new StringDeserializer()) {
                        requestId.set(s.deserialize(kafkaTopic.generateHashTopic().name(), requestIdBytes));
                    }
                },
                () -> {
                    log.warn("RequestId not present in header");
                    returnBecauseOfHeaderNotPresent.set(true);
                }
        );
        if (returnBecauseOfHeaderNotPresent.get()) {
            return;
        }

        log.info("Saved Paste: {}", paste.get());
        // Тут надо записать в БД новую запись, а именно - id hash, text varchar, datetime time
        pasteDbService.savePaste(paste.get(), hash);

        PasteEntity pasteEntity = new PasteEntity(
                hash,
                paste.get().getCreateTime().getTime(),
                paste.get().getPaste()
        );

        // Отправить на фронт информацию о пасте.
        futurePasteComponent.completeFuture(UUID.fromString(requestId.get()), pasteEntity);
    }

    private Optional<byte[]> getHeader(Headers headers, String headerName) {
        Iterator<Header> header = headers.headers(headerName).iterator();
        if (header.hasNext()) {
            byte[] returnValue = header.next().value();
            if (header.hasNext()) {
                log.warn("Headers has multiple {} headers.", headerName);
            }
            return Optional.of(returnValue);
        } else {
            return Optional.empty();
        }
    }
}
