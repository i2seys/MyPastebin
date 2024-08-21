package ru.pastebin.cli.benchmark;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.pastebin.cli.benchmark.bench.BenchConfiguration;
import ru.pastebin.cli.benchmark.bench.BenchFactory;
import ru.pastebin.cli.benchmark.cliParser.CommandLineParser;


@Slf4j
@Component
public class BenchmarkApplication {
    private final BenchFactory benchFactory;
    private final CommandLineParser commandLineParser;

    @Autowired
    public BenchmarkApplication(
            BenchFactory benchFactory,
            CommandLineParser commandLineParser
    ) {
        this.benchFactory = benchFactory;
        this.commandLineParser = commandLineParser;
    }

    public void bench(String[] args) {
        BenchConfiguration benchConfiguration = commandLineParser.getBenchRecord(args);
        runBench(benchConfiguration);
    }

    private void runBench(BenchConfiguration benchRecord) {
        benchFactory.getBench(benchRecord).runBench();
    }
}
