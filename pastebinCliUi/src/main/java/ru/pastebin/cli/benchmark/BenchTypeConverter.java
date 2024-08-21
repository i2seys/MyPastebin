package ru.pastebin.cli.benchmark;

import org.apache.commons.cli.Converter;
import org.apache.commons.cli.ParseException;
import org.springframework.stereotype.Component;
import ru.pastebin.cli.benchmark.bench.BenchConfiguration;

@Component
public class BenchTypeConverter implements Converter<BenchConfiguration.BenchTypePair, ParseException> {
    @Override
    public BenchConfiguration.BenchTypePair apply(String s) throws ParseException {
        if (s == null) {
            throw new ParseException("Wrong input args - string cant be null.");
        }
        String[] args = s.split("=");
        if (args.length != 2) {
            throw new ParseException("Wrong input args - should be size of 2, split char equals to '='.");
        }
        BenchType benchType = switch (args[0]) {
            case "q" -> BenchType.QUERY_COUNT;
            case "t" -> BenchType.BENCH_TIME;
            default -> throw new ParseException("Wrong bench type");
        };
        long value;
        try {
            value = Long.parseLong(args[1]);
        } catch (Exception e) {
            throw new ParseException(e.getMessage());
        }
        return new BenchConfiguration.BenchTypePair(benchType, value);
    }
}
