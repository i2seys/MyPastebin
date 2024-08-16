package ru.pastebin.pastebinMicroservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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
}
