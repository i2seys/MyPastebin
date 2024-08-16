package ru.pastebin.pastebinMicroservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pastebin.pastebinMicroservice.dto.Paste;
import ru.pastebin.pastebinMicroservice.kafka.producer.KafkaProducer;

@RestController()
@RequestMapping("/api/v1")
public class PasteController {
    private final KafkaProducer kafkaProducer;

    @Autowired
    public PasteController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PutMapping("create_paste")
    public void createPaste(@RequestBody Paste paste) {
        kafkaProducer.sendGetHashMessage(paste);
    }
}
