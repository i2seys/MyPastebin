package ru.pastebin.cli.benchmark;

import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;
import ru.pastebin.cli.benchmark.bench.BenchConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BenchTypeConverterTest {
    private final BenchTypeConverter benchTypeConverter = new BenchTypeConverter();
    @Test
    void apply_correctString() throws ParseException {
        String correctQ = "q=1000";
        String correctT = "t=10000";
        var expectedQ = new BenchConfiguration.BenchTypePair(BenchType.QUERY_COUNT, 1000);
        var expectedT = new BenchConfiguration.BenchTypePair(BenchType.BENCH_TIME, 10000);

        var actualT = benchTypeConverter.apply(correctT);
        var actualQ = benchTypeConverter.apply(correctQ);

        assertEquals(expectedT, actualT);
        assertEquals(expectedQ, actualQ);
    }

    @Test
    void apply_wrongSeparator_shouldThrowParseException() {
        String badQ = "q.1000";
        String badT = "t/10000";

        assertThrows(ParseException.class, () -> benchTypeConverter.apply(badT));
        assertThrows(ParseException.class, () -> benchTypeConverter.apply(badQ));
    }

    @Test
    void apply_emptyString_shouldThrowParseException() {
        String bad = "";

        assertThrows(ParseException.class, () -> benchTypeConverter.apply(bad));
    }

    @Test
    void apply_nullString_shouldThrowParseException() {
        String bad = null;

        assertThrows(ParseException.class, () -> benchTypeConverter.apply(bad));
    }

    @Test
    void apply_wrongBenchType_shouldThrowParseException() {
        String bad1 = "a=1000";
        String bad2 = "-=10000";

        assertThrows(ParseException.class, () -> benchTypeConverter.apply(bad1));
        assertThrows(ParseException.class, () -> benchTypeConverter.apply(bad2));
    }

    @Test
    void apply_wrongBenchValue_shouldThrowParseException() {
        String badQ = "q=a";
        String badT = "t=][1aa234";

        assertThrows(ParseException.class, () -> benchTypeConverter.apply(badQ));
        assertThrows(ParseException.class, () -> benchTypeConverter.apply(badT));
    }
}
