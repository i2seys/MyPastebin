package ru.pastebin.pastebinMicroservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pastebin.pastebinMicroservice.dto.Paste;
import ru.pastebin.pastebinMicroservice.model.PasteEntity;
import ru.pastebin.pastebinMicroservice.service.PasteKafkaService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController()
@RequestMapping("/api/v1")
public class PasteController {
    private final PasteKafkaService pasteKafkaService;

    @Autowired
    public PasteController(PasteKafkaService pasteKafkaService) {
        this.pasteKafkaService = pasteKafkaService;
    }

    @PutMapping("create_paste")
    public ResponseEntity<?> createPaste(@RequestBody Paste paste) {
        Future<PasteEntity> newPaste = pasteKafkaService.savePasteWithFuture(paste);
        try {
            // Ожидание сохранения paste.
            return ResponseEntity.ok(newPaste.get());
        } catch (InterruptedException | ExecutionException e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
