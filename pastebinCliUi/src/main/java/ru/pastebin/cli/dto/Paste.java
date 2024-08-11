package ru.pastebin.cli.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Paste {
    public String paste;
    @JsonProperty("create_time")
    public Date createTime;
}