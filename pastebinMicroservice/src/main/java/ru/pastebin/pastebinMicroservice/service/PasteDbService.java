package ru.pastebin.pastebinMicroservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pastebin.pastebinMicroservice.dto.PasteRequest;
import ru.pastebin.pastebinMicroservice.model.PasteEntity;
import ru.pastebin.pastebinMicroservice.repository.PasteRepository;

import java.util.Optional;

@Service
public class PasteDbService {
    private final PasteRepository pasteRepository;

    @Autowired
    public PasteDbService(PasteRepository pasteRepository) {
        this.pasteRepository = pasteRepository;
    }

    public void savePaste(PasteRequest pasteRequest, String hash) {
        pasteRepository.save(new PasteEntity(
                hash,
                pasteRequest.getCreateTime().getTime(),
                pasteRequest.getPaste()
        ));
    }

    public Optional<PasteEntity> getPaste(String id) {
        return pasteRepository.findById(id);
    }
}
