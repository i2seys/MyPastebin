package ru.pastebin.cli.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PasteWithHash {
    public String hash;
    public String paste;
    @JsonProperty("create_time")
    public long createTime;

    public PasteWithHash(String hash, String paste, long createTime) {
        this.hash = hash;
        this.paste = paste;
        this.createTime = createTime;
    }

    public PasteWithHash() {
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPaste() {
        return paste;
    }

    public void setPaste(String paste) {
        this.paste = paste;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "PasteWithHash{" +
                "hash='" + hash + '\'' +
                ", paste='" + paste + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
