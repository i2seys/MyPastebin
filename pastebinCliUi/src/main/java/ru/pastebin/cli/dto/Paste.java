package ru.pastebin.cli.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Paste {
    public String paste;
    @JsonProperty("create_time")
    public long createTime;
}