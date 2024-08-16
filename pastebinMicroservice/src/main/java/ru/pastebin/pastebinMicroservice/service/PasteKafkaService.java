package ru.pastebin.pastebinMicroservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pastebin.pastebinMicroservice.component.FuturePasteComponent;
import ru.pastebin.pastebinMicroservice.dto.Paste;
import ru.pastebin.pastebinMicroservice.kafka.producer.KafkaProducer;
import ru.pastebin.pastebinMicroservice.model.PasteEntity;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
public class PasteKafkaService {
    private final KafkaProducer kafkaProducer;
    private final FuturePasteComponent futurePasteComponent;

    @Autowired
    public PasteKafkaService(
            KafkaProducer kafkaProducer,
            FuturePasteComponent futurePasteComponent
    ) {
        this.kafkaProducer = kafkaProducer;
        this.futurePasteComponent = futurePasteComponent;
    }

    public Future<PasteEntity> savePasteWithFuture(Paste paste) {
        UUID requestId = UUID.randomUUID();
        CompletableFuture<PasteEntity> futurePaste = new CompletableFuture<>();

        kafkaProducer.sendGetHashMessage(paste, requestId);
        //TODO: рефакторинг - надо будет правильно написать логику ожидания сохранения пасты в БД.
        // Для того, чтобы отслеживать сохранение paste в БД.
        futurePasteComponent.putPasteFuture(requestId, futurePaste);
        return futurePaste;
    }
}
