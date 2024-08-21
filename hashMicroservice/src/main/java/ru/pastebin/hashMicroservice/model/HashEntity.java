package ru.pastebin.hashMicroservice.model;

import jakarta.persistence.*;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashEntity that = (HashEntity) o;
        return Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(hash);
    }
}
