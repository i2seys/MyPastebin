package ru.pastebin.hashMicroservice.service;

public interface HashGeneratorService {
    String generateAndSaveHash();
    String generateRandomHash();
}
