package ru.pastebin.hashMicroservice.service;

import io.github.thibaultmeyer.cuid.CUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.pastebin.hashMicroservice.model.HashEntity;
import ru.pastebin.hashMicroservice.repository.HashRepository;

@Slf4j
@Service
public class HashGenerator {
    private final int hashLength;
    private final HashRepository hashRepository;

    @Autowired
    public HashGenerator(
            HashRepository hashRepository,
            @Value("${hash.length}") int hashLength) {
        this.hashRepository = hashRepository;
        this.hashLength = hashLength;
    }

    /**
     * Возвращает уникальный в рамках таблицы хэш.
     * */
    public String generateAndSaveHash() {
        //1) Генерация случайного cuid
        HashEntity randomHash = generateRandomHash();

        while (hashRepository.existsById(randomHash.getHash())) {
            log.info("Hash already exist [{}], repeat...", randomHash.getHash());
            randomHash = generateRandomHash();
        }

        hashRepository.save(randomHash);
        return randomHash.getHash();
    }
    private HashEntity generateRandomHash() {
        return new HashEntity(CUID.randomCUID2(hashLength).toString());
    }
}
