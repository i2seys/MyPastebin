package ru.pastebin.hashMicroservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.pastebin.hashMicroservice.model.HashEntity;

@Repository
public interface HashRepository extends CrudRepository<HashEntity, String> {
}
