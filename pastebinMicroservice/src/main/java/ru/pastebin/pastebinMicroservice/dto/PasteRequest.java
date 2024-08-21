package ru.pastebin.pastebinMicroservice.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class PasteRequest {
    private String paste;
    @JsonProperty("create_time")
    private Date createTime;
}
