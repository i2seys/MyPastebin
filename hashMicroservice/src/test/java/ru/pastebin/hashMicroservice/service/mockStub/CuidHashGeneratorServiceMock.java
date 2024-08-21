package ru.pastebin.hashMicroservice.service.mockStub;

import ru.pastebin.hashMicroservice.repository.HashRepository;
import ru.pastebin.hashMicroservice.service.CuidHashGeneratorService;

import java.util.function.Supplier;

public class CuidHashGeneratorServiceMock extends CuidHashGeneratorService {
    private final Supplier<String> defaultHashGenerateFunction = super::generateRandomHash;
    private Supplier<String> currentHashGenerateFunction;
    public CuidHashGeneratorServiceMock(HashRepository hashRepository, int hashSize) {
        super(hashRepository, hashSize);
        currentHashGenerateFunction = defaultHashGenerateFunction;
    }

    public void setGeneratedHashForOneTime(String generatedHash) {
        this.currentHashGenerateFunction = () -> generatedHash;
    }

    @Override
    public String generateRandomHash() {
        String hash = currentHashGenerateFunction.get();
        currentHashGenerateFunction = defaultHashGenerateFunction;
        return hash;
    }
}
