package ru.pastebin.cli.benchmark.cliParser;

import org.apache.commons.cli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.pastebin.cli.benchmark.BenchHttpQueryType;
import ru.pastebin.cli.benchmark.BenchTypeConverter;
import ru.pastebin.cli.benchmark.bench.BenchConfiguration;

@Component
public class CommandLineParser {
    private final BenchTypeConverter benchTypeConverter;

    @Autowired
    public CommandLineParser(BenchTypeConverter benchTypeConverter) {
        this.benchTypeConverter = benchTypeConverter;
    }

    /** Принимает на вход args и возвращает объект с параметрами для бенчмарка. */
    public BenchConfiguration getBenchRecord(String[] args) {
        CommandLine cmd = getCmdOptions(args);
        try {
            BenchHttpQueryType benchHttpQueryType = cmd.getParsedOptionValue("bench");
            BenchConfiguration.BenchTypePair benchTypePair = cmd.getParsedOptionValue("bench-type");

            int threads = Math.toIntExact(cmd.getParsedOptionValue("threads"));
            if (threads <= 0) {
                throw new ParseException("Количество потоков должно быть более 0.");
            }
            int maxTextSize = Math.toIntExact(cmd.getParsedOptionValue("max-text-size"));
            if (maxTextSize <= 0) {
                throw new ParseException("Максимальная длина текста должна быть не менее 0.");
            }
            int minTextSize = Math.toIntExact(cmd.getParsedOptionValue("min-text-size"));
            if (minTextSize <= 0) {
                throw new ParseException("Минимальная длина текста должна быть не менее 0.");
            }
            if (minTextSize > maxTextSize) {
                throw new RuntimeException("Минимальная длина текста не может быть больше максимаьной.");
            }
            Double percentageCreate = getNullableDoubleOption("percentage-create", cmd);
            if (percentageCreate != null && (percentageCreate <= 0.0 || percentageCreate >= 1.0)) {
                throw new ParseException("Процент запросов на создание должен быть в диапазоне (0;1) не включительно.");
            }
            Integer minDelay = getNullableIntOption("min-delay", cmd);
            if (minDelay != null && minDelay < 0) {
                throw new ParseException("Минимальная задержка между запросами не может быть меньше 0.");
            }
            Integer maxDelay = getNullableIntOption("max-delay", cmd);
            if (maxDelay != null && maxDelay < 0) {
                throw new ParseException("Максимальная задержка между запросами не может быть меньше 0.");
            }
            if ((maxDelay != null && minDelay != null) && (maxDelay < minDelay)) {
                throw new ParseException("Максимальная задержка должна быть больше, чем минимальная");
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

    private CommandLine getCmdOptions(String[] args) {
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

        OptionGroup delayGroup = new OptionGroup();
        Option maxDelay = Option.builder()
                .longOpt("max-delay")
                .hasArg(true)
                .desc( "максимальная задержка между запросами (в мс)")
                .converter(Converter.NUMBER)
                .build();
        Option minDelay = Option.builder()
                .longOpt("min-delay")
                .hasArg(true)
                .desc( "минимальная задержка между запросами (в мс)")
                .converter(Converter.NUMBER)
                .build();
        delayGroup.addOption(minDelay);
        delayGroup.addOption(maxDelay);
        delayGroup.setRequired(false);

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

        org.apache.commons.cli.CommandLineParser parser = new DefaultParser();
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
    private Double getNullableDoubleOption(String name, CommandLine cmd) throws ParseException {
        if (cmd.hasOption(name)) {
            Number option = cmd.getParsedOptionValue(name);
            return option.doubleValue();
        } else {
            return null;
        }
    }
    private Integer getNullableIntOption(String name, CommandLine cmd) throws ParseException {
        if (cmd.hasOption(name)) {
            Number option = cmd.getParsedOptionValue(name);
            return option.intValue();
        } else {
            return null;
        }
    }
}
