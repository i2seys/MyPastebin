package ru.pastebin.pastebinMicroservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "paste")
public class PasteEntity {
    @Id
    @Column(name = "hash")
    private String hash;
    @Column(name = "create_time")
    private long createTime;
    @Column(name = "paste")
    private String paste;

    public PasteEntity(String hash, long createTime, String paste) {
        this.hash = hash;
        this.createTime = createTime;
        this.paste = paste;
    }

    public PasteEntity() {
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getPaste() {
        return paste;
    }

    public void setPaste(String paste) {
        this.paste = paste;
    }
}
