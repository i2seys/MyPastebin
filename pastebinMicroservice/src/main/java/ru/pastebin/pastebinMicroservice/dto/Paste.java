package ru.pastebin.pastebinMicroservice.dto;


import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Paste {
    public String paste;
    public Date date;
}
