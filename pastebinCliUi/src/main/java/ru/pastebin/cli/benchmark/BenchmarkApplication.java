package ru.pastebin.cli.benchmark;

import org.apache.commons.cli.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.pastebin.cli.service.PasteService;


public class BenchmarkApplication {
    private static PasteService pasteService;
    public static void bench(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext("ru.pastebin.cli");
        pasteService = ctx.getBean(PasteService.class);

        CommandLine cmd = getOptions(args);
        BenchRecord benchRecord = getBenchRecord(cmd);
        runBench(benchRecord);
    }

    private static void runBench(BenchRecord benchRecord) {
        switch (benchRecord.benchType) {
            case CREATE -> {
                return;
            }
            case GET -> {
                return;
            }
            case CREATE_AND_GET -> {
                return;
            }
        }
    }

    private static BenchRecord getBenchRecord(CommandLine cmd) {
        try {
            BenchType benchType = cmd.getParsedOptionValue("bench");
            int queryCount = cmd.getParsedOptionValue("query-count");
            int threads = cmd.getParsedOptionValue("threads");
            int maxTextSize = cmd.getParsedOptionValue("max-text-size");
            int minTextSize = cmd.getParsedOptionValue("min-text-size");
            double percentageCreate = cmd.getParsedOptionValue("percentage-create");
            double minDelay;
            if (cmd.hasOption("min-delay")) {
                minDelay = cmd.getParsedOptionValue("min-delay");
            } else {
                minDelay = 0;
            }
            double maxDelay;
            if (cmd.hasOption("max-delay")) {
                maxDelay = cmd.getParsedOptionValue("max-delay");
            } else {
                maxDelay = 0;
            }
            return new BenchRecord(
                    benchType,
                    queryCount,
                    threads,
                    maxTextSize,
                    minTextSize,
                    percentageCreate,
                    minDelay,
                    maxDelay
            );
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            System.exit(1);
            return null;
        }
    }

    private static CommandLine getOptions(String[] args) {
        Options options = new Options();

        Option createPastes = Option.builder()
                .option("b")
                .longOpt("bench")
                .hasArg(true)
                .required(true)
                .desc("вид теста (c - только создание, g - только получение, x - и то, и то)")
                .converter(BenchType.BENCH_CONVERTER)
                .build();
        options.addOption(createPastes);


        //Один из двух ниже
        OptionGroup queryCountOrBenchTime = new OptionGroup();
        Option queryCount = Option.builder()
                .longOpt("query-count")
                .hasArg(true)
                .desc("количество запросов")
                .converter(Converter.NUMBER)
                .build();
        Option benchTime = Option.builder()
                .longOpt("bench-time")
                .hasArg(true)
                .desc("время тестирования (в секундах)")
                .converter(Converter.NUMBER)
                .build();
        queryCountOrBenchTime.addOption(queryCount).addOption(benchTime);
        queryCountOrBenchTime.setRequired(true);
        options.addOptionGroup(queryCountOrBenchTime);

        Option threadsCount = Option.builder()
                .option("t")
                .longOpt("threads")
                .hasArg(true)
                .required(true)
                .desc( "количество потоков")
                .converter(Converter.NUMBER)
                .build();
        Option maxTextSize = Option.builder()
                .longOpt("max-text-size")
                .hasArg(true)
                .required(true)
                .desc( "максимальный размер текста")
                .converter(Converter.NUMBER)
                .build();
        Option minTextSize = Option.builder()
                .longOpt("min-text-size")
                .hasArg(true)
                .required(true)
                .desc( "минимальный размер текста")
                .converter(Converter.NUMBER)
                .build();
        Option maxDelay = Option.builder()
                .longOpt("max-delay")
                .hasArg(true)
                .required(false)
                .desc( "максимальная задержка между запросами (в мс)")
                .converter(Converter.NUMBER)
                .build();
        Option minDelay = Option.builder()
                .longOpt("min-delay")
                .hasArg(true)
                .required(false)
                .desc( "минимальная задержка между запросами (в мс)")
                .converter(Converter.NUMBER)
                .build();
        Option percentageOfCreate = Option.builder()
                .longOpt("percentage-create")
                .hasArg(true)
                .required(false)
                .desc( "доля создания паст (должна быть больше 0 и меньше 0.5), обязательна для -x")
                .converter(Converter.NUMBER)
                .build();

        options
                .addOption(threadsCount)
                .addOption(maxTextSize)
                .addOption(minTextSize)
                .addOption(maxDelay)
                .addOption(minDelay)
                .addOption(percentageOfCreate);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptionComparator(null);
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("benchmark", options);
            System.exit(1);
        }
        return cmd;
    }

    private record BenchRecord(
            BenchType benchType,
            int queryCount,
            int threads,
            int maxTextSize,
            int minTextSize,
            double percentageCreate,
            double minDelay,
            double maxDelay
    ){}
}
