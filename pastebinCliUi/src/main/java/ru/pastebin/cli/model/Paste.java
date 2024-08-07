package ru.pastebin.cli.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
public class Paste {
    public String paste;
    public Date date;
}