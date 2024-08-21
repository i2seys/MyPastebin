package ru.pastebin.hashMicroservice.service;

import com.google.common.annotations.VisibleForTesting;
import io.github.thibaultmeyer.cuid.CUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.pastebin.hashMicroservice.model.HashEntity;
import ru.pastebin.hashMicroservice.repository.HashRepository;

@Slf4j
@Service
public class CuidHashGeneratorService implements HashGeneratorService {
    private final int hashLength;
    private final HashRepository hashRepository;

    @Autowired
    public CuidHashGeneratorService(
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
        HashEntity randomHash = new HashEntity(generateRandomHash());

        while (hashRepository.existsById(randomHash.getHash())) {
            log.info("Hash already exist [{}], repeat...", randomHash.getHash());
            randomHash = new HashEntity(generateRandomHash());
        }

        hashRepository.save(randomHash);
        return randomHash.getHash();
    }

    public String generateRandomHash() {
        return CUID.randomCUID2(hashLength).toString();
    }
}
