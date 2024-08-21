package ru.pastebin.cli.benchmark.cliParser;

import org.junit.jupiter.api.Test;
import ru.pastebin.cli.benchmark.BenchType;
import ru.pastebin.cli.benchmark.BenchTypeConverter;
import ru.pastebin.cli.benchmark.bench.BenchConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.pastebin.cli.benchmark.BenchHttpQueryType.*;

public class CommandLineParserTest {
    private final BenchTypeConverter benchTypeConverter = new BenchTypeConverter();
    private final CommandLineParser commandLineParser = new CommandLineParser(benchTypeConverter);

    @Test
    void getBenchRecord_success() {
        String[] args1 = new String[]{"-b", "c", "-bt", "q=1", "-t", "2", "--max-text-size", "3", "--min-text-size", "2"};
        String[] args2 = new String[]{"-b", "x", "-bt", "t=1", "-t", "2", "--max-text-size", "3", "--min-text-size", "2", "--percentage-create", "0.4"};
        String[] args3 = new String[]{"-b", "g", "-bt", "q=1", "-t", "2", "--max-text-size", "3", "--min-text-size", "2", "--max-delay","5", "--min-delay", "2"};
        var expected1 = new BenchConfiguration(
                CREATE,
                new BenchConfiguration.BenchTypePair(BenchType.QUERY_COUNT, 1),
                2,
                3,
                2,
                null,
                null,
                null
        );
        var expected2 = new BenchConfiguration(
                CREATE_AND_GET,
                new BenchConfiguration.BenchTypePair(BenchType.BENCH_TIME, 1),
                2,
                3,
                2,
                0.4,
                null,
                null
        );
        var expected3 = new BenchConfiguration(
                GET,
                new BenchConfiguration.BenchTypePair(BenchType.QUERY_COUNT, 1),
                2,
                3,
                2,
                null,
                2,
                5
        );

        BenchConfiguration actualConf1 = commandLineParser.getBenchRecord(args1);
        BenchConfiguration actualConf2 = commandLineParser.getBenchRecord(args2);
        BenchConfiguration actualConf3 = commandLineParser.getBenchRecord(args3);

        assertEquals(actualConf1, expected1);
        assertEquals(actualConf2, expected2);
        assertEquals(actualConf3, expected3);
    }
}
