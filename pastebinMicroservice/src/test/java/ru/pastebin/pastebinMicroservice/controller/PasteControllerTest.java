package ru.pastebin.pastebinMicroservice.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.pastebin.pastebinMicroservice.dto.PasteRequest;
import ru.pastebin.pastebinMicroservice.dto.PasteResponse;
import ru.pastebin.pastebinMicroservice.model.PasteEntity;
import ru.pastebin.pastebinMicroservice.service.PasteDbService;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import ru.pastebin.pastebinMicroservice.service.PasteKafkaService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PasteControllerTest {
    private static PasteKafkaService pasteKafkaService;
    private static PasteDbService pasteDbService;
    private static PasteController pasteController;
    private static final PasteRequest pasteRequest = new PasteRequest("123", new Date());
    private static final String existedPasteEntityHash = "aaaaaaaa";
    private static final String notExistedPasteEntityHash = "bbbbbbbb";
    private static final PasteEntity pasteEntity = new PasteEntity(existedPasteEntityHash, System.currentTimeMillis(), "123");
    private static CompletableFuture<PasteEntity> futurePasteEntity;
    @BeforeAll
    static void init() throws ExecutionException, InterruptedException {
        futurePasteEntity = mock(CompletableFuture.class);
        when(futurePasteEntity.get()).thenReturn(pasteEntity);
        pasteKafkaService = mock(PasteKafkaService.class);
        when(pasteKafkaService.savePasteWithFuture(pasteRequest)).thenReturn(futurePasteEntity);
        pasteDbService = mock(PasteDbService.class);
        when(pasteDbService.getPaste(existedPasteEntityHash)).thenReturn(Optional.of(pasteEntity));
        when(pasteDbService.getPaste(notExistedPasteEntityHash)).thenReturn(Optional.empty());

        pasteController = new PasteController(pasteKafkaService, pasteDbService);
    }
    @Test
    void createPaste_success() throws ExecutionException, InterruptedException {
        pasteController.createPaste(pasteRequest);

        verify(pasteKafkaService, times(1)).savePasteWithFuture(pasteRequest);
        verify(futurePasteEntity, times(1)).get();
    }
    @Test
    void getPaste_success() {
        ResponseEntity<?> response = pasteController.getPaste(existedPasteEntityHash);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        PasteResponse expectedPaste = (PasteResponse) response.getBody();
        assertNotNull(expectedPaste);

        assertEquals(expectedPaste.getPaste(), pasteEntity.getPaste());
        assertEquals(expectedPaste.getHash(), pasteEntity.getHash());
        assertEquals(expectedPaste.getCreateTime(), pasteEntity.getCreateTime());
        verify(pasteDbService, times(1)).getPaste(existedPasteEntityHash);
    }
    @Test
    void getPaste_notExist() {
        ResponseEntity<?> response = pasteController.getPaste(notExistedPasteEntityHash);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);

        verify(pasteDbService, times(1)).getPaste(notExistedPasteEntityHash);
    }
}
