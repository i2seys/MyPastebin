package ru.pastebin.pastebinMicroservice.component;

import org.springframework.stereotype.Component;
import ru.pastebin.pastebinMicroservice.model.PasteEntity;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * Хранит в себе информацию об id запросов и их коллбеках.
 * Когда цепочка сохранения завершается, то коллбек должен активироваться.
 * */
@Component
public class FuturePasteComponent {
    private final Map<UUID, CompletableFuture<PasteEntity>> requestIdToFutureMap;

    public FuturePasteComponent() {
        requestIdToFutureMap = new ConcurrentHashMap<>();
    }

    /**
     * Функицю надо вызывать когда начинается цикл создания пасты.
     * */
    public void putPasteFuture(UUID requestId, CompletableFuture<PasteEntity> pasteEntityFuture) {
        requestIdToFutureMap.put(requestId, pasteEntityFuture);
    }

    public void completeFuture(UUID requestId, PasteEntity pasteEntity) {
        requestIdToFutureMap.get(requestId).complete(pasteEntity);
    }
}
