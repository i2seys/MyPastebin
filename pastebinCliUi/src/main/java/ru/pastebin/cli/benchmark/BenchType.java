package ru.pastebin.cli.benchmark;

import org.apache.commons.cli.Converter;

public enum BenchType {
    CREATE,
    GET,
    CREATE_AND_GET;

    public static final Converter<BenchType, ?> BENCH_CONVERTER = (Converter<BenchType, Throwable>) s -> switch (s) {
            case "c" -> CREATE;
            case "g" -> GET;
            case "x" -> CREATE_AND_GET;
            default -> throw new IllegalArgumentException("Wrong bench type");
    };
}
