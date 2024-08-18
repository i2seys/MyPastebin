package ru.pastebin.cli;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.pastebin.cli.benchmark.BenchmarkApplication;
import ru.pastebin.cli.cliUi.CliUiApplication;

@Slf4j
public class MainApplication {
    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext("ru.pastebin.cli");
        if (args.length > 0) {
            BenchmarkApplication benchmarkApplication = ctx.getBean(BenchmarkApplication.class);
            benchmarkApplication.bench(args);
        } else {
            CliUiApplication cliUi = ctx.getBean(CliUiApplication.class);
            cliUi.run();
        }
    }
}
