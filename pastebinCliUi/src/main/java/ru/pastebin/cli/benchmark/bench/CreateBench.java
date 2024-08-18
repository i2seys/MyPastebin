package ru.pastebin.cli.benchmark.bench;

import ru.pastebin.cli.benchmark.RandomPasteCreator;
import ru.pastebin.cli.dto.Paste;
import ru.pastebin.cli.service.PasteService;

public class CreateBench extends Bench {
    private final PasteService pasteService;
    private final RandomPasteCreator randomPasteCreator;

    public CreateBench(
            BenchConfiguration benchConfiguration,
            PasteService pasteService,
            RandomPasteCreator randomPasteCreator
    ) {
        super(benchConfiguration);
        this.pasteService = pasteService;
        this.randomPasteCreator = randomPasteCreator;
    }

    @Override
    protected void singleBenchAction() {
        BenchConfiguration benchConfiguration = getBenchConfiguration();
        Paste paste = randomPasteCreator.createPaste(benchConfiguration.minTextSize(), benchConfiguration.maxTextSize());
        pasteService.sendPaste(paste);
    }
}
