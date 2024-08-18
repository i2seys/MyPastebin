package ru.pastebin.cli.benchmark;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.pastebin.cli.benchmark.bench.BenchConfiguration;
import ru.pastebin.cli.benchmark.bench.BenchFactory;
import ru.pastebin.cli.service.PasteService;


@Slf4j
@Component
public class BenchmarkApplication {
    private final PasteService pasteService;
    private final BenchTypeConverter benchTypeConverter;
    private final BenchFactory benchFactory;
    @Autowired
    public BenchmarkApplication(
            PasteService pasteService,
            BenchTypeConverter benchTypeConverter,
            BenchFactory benchFactory
    ) {
        this.pasteService = pasteService;
        this.benchTypeConverter = benchTypeConverter;
        this.benchFactory = benchFactory;
    }

    public void bench(String[] args) {
        CommandLine cmd = getOptions(args);
        BenchConfiguration benchConfiguration = getBenchRecord(cmd);
        runBench(benchConfiguration);
    }

    private void runBench(BenchConfiguration benchRecord) {
        benchFactory.getBench(benchRecord).runBench();
    }

    private CommandLine getOptions(String[] args) {
        Options options = new Options();

        Option createPastes = Option.builder()
                .option("b")
                .longOpt("bench")
                .hasArg(true)
                .required(true)
                .desc("вид теста (c - только создание, g - только получение, x - и то, и то)")
                .converter(BenchHttpQueryType.BENCH_CONVERTER)
                .build();
        options.addOption(createPastes);

        Option queryCountOrBenchTime = Option.builder()
                .option("bt")
                .longOpt("bench-type")
                .hasArgs()
                .numberOfArgs(1)//TODO: возможно стоит переделать под какие-то другие аргументы
                .required(true)
                .converter(benchTypeConverter)
                .desc("первый аргумент - количество запросов (q) или время тестирования (t), " +
                        "второй - величина (количество или время). Пример: q=10000")
                .build();
        options.addOption(queryCountOrBenchTime);

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

    private BenchConfiguration getBenchRecord(CommandLine cmd) {
        try {
            BenchHttpQueryType benchHttpQueryType = cmd.getParsedOptionValue("bench");
            BenchConfiguration.BenchTypePair benchTypePair = cmd.getParsedOptionValue("bench-type");

            long threads = cmd.getParsedOptionValue("threads");
            long maxTextSize = cmd.getParsedOptionValue("max-text-size");
            long minTextSize = cmd.getParsedOptionValue("min-text-size");
            double percentageCreate;
            if (cmd.hasOption("percentage-create")) {
                percentageCreate = cmd.getParsedOptionValue("percentage-create");
            } else {
                percentageCreate = 0;
            }
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
            return new BenchConfiguration(
                    benchHttpQueryType,
                    benchTypePair,
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
}
