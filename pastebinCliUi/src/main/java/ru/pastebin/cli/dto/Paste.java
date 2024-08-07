package ru.pastebin.cli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Paste {
    public String paste;
    public Date date;
}