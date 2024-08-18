package ru.pastebin.cli.benchmark.bench;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.pastebin.cli.benchmark.RandomPasteCreator;
import ru.pastebin.cli.service.PasteService;

@Component
public class BenchFactory {
    private final PasteService pasteService;
    private final RandomPasteCreator randomPasteCreator;

    @Autowired
    public BenchFactory(PasteService pasteService, RandomPasteCreator randomPasteCreator) {
        this.pasteService = pasteService;
        this.randomPasteCreator = randomPasteCreator;
    }

    public Bench getBench(BenchConfiguration benchConfiguration) {
        switch (benchConfiguration.benchHttpQueryType()) {
            case CREATE -> {
                return new CreateBench(benchConfiguration, pasteService, randomPasteCreator);
            }
            case GET -> throw new UnsupportedOperationException();
            case CREATE_AND_GET -> throw new UnsupportedOperationException();
        }
        return null;
    }
}
