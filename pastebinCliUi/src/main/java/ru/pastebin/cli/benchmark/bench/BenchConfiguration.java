package ru.pastebin.cli.benchmark.bench;

import ru.pastebin.cli.benchmark.BenchHttpQueryType;
import ru.pastebin.cli.benchmark.BenchType;

import javax.annotation.Nullable;

public record BenchConfiguration(
        BenchHttpQueryType benchHttpQueryType,
        BenchTypePair benchTypePair,
        int threads,
        int maxTextSize,
        int minTextSize,
        @Nullable Double percentageCreate,
        @Nullable Integer minDelay,
        @Nullable Integer maxDelay
){
  public record BenchTypePair(BenchType benchType, long value) {}
}
