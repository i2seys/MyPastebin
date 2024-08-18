package ru.pastebin.cli.benchmark.bench;

import jakarta.annotation.Nullable;
import ru.pastebin.cli.benchmark.BenchHttpQueryType;
import ru.pastebin.cli.benchmark.BenchType;

public record BenchConfiguration(
        BenchHttpQueryType benchHttpQueryType,
        BenchTypePair benchTypePair,
        long threads,
        long maxTextSize,
        long minTextSize,
        double percentageCreate,
        double minDelay,
        double maxDelay
){
  public record BenchTypePair(BenchType benchType, long value) {}
}
