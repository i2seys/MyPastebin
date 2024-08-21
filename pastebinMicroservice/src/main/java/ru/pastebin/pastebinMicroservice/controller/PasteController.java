package ru.pastebin.pastebinMicroservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pastebin.pastebinMicroservice.dto.PasteRequest;
import ru.pastebin.pastebinMicroservice.dto.PasteResponse;
import ru.pastebin.pastebinMicroservice.model.PasteEntity;
import ru.pastebin.pastebinMicroservice.service.PasteDbService;
import ru.pastebin.pastebinMicroservice.service.PasteKafkaService;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/api/v1")
public class PasteController {
    private final PasteKafkaService pasteKafkaService;
    private final PasteDbService pasteDbService;

    @Autowired
    public PasteController(PasteKafkaService pasteKafkaService, PasteDbService pasteDbService) {
        this.pasteKafkaService = pasteKafkaService;
        this.pasteDbService = pasteDbService;
    }

    @PutMapping("create_paste")
    public ResponseEntity<?> createPaste(@RequestBody PasteRequest pasteRequest) {
        Future<PasteEntity> newPaste = pasteKafkaService.savePasteWithFuture(pasteRequest);
        try {
            // Ожидание сохранения paste.
            return ResponseEntity.ok(new PasteResponse(newPaste.get()));
        } catch (InterruptedException | ExecutionException e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("get_paste/{id}")
    public ResponseEntity<?> getPaste(@PathVariable String id) {
        Optional<PasteEntity> paste = pasteDbService.getPaste(id);
        if (paste.isPresent()) {
            return ResponseEntity.ok(new PasteResponse(paste.get()));
        } else {
            return new ResponseEntity<>("Paste is not present.", HttpStatus.NOT_FOUND);
        }
    }
}
