package ru.pastebin.pastebinMicroservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.pastebin.pastebinMicroservice.model.PasteEntity;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PasteResponse {
    private String hash;
    @JsonProperty("create_time")
    private long createTime;
    private String paste;

    public PasteResponse(PasteEntity pasteEntity) {
        this.hash = pasteEntity.getHash();
        this.paste = pasteEntity.getPaste();
        this.createTime = pasteEntity.getCreateTime();
    }
}
