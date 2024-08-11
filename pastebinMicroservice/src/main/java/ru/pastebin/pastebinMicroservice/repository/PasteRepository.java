package ru.pastebin.pastebinMicroservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.pastebin.pastebinMicroservice.model.PasteEntity;


@Repository
public interface PasteRepository extends CrudRepository<PasteEntity, String> {
}
