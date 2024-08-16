package ru.pastebin.pastebinMicroservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pastebin.pastebinMicroservice.dto.Paste;
import ru.pastebin.pastebinMicroservice.model.PasteEntity;
import ru.pastebin.pastebinMicroservice.repository.PasteRepository;

@Service
@Slf4j
public class PasteDbService {
    private final PasteRepository pasteRepository;

    @Autowired
    public PasteDbService(PasteRepository pasteRepository) {
        this.pasteRepository = pasteRepository;
    }

    public void savePaste(Paste paste, String hash) {
        pasteRepository.save(new PasteEntity(
                hash,
                paste.getCreateTime().getTime(),
                paste.getPaste()
        ));
    }
}
