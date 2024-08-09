package ru.pastebin.hashMicroservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "hash")
public class HashEntity {
    @Id
    @Column(name = "hash")
    private String hash;

    public HashEntity(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public HashEntity() {
    }
}
