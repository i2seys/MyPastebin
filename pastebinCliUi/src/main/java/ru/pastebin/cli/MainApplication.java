package ru.pastebin.cli;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.HttpClientErrorException;
import ru.pastebin.cli.benchmark.BenchmarkApplication;
import ru.pastebin.cli.cliUi.CliUiApplication;
import ru.pastebin.cli.dto.Paste;
import ru.pastebin.cli.dto.PasteWithHash;
import ru.pastebin.cli.service.PasteService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

@Slf4j
public class MainApplication {
    public static void main(String[] args) {
        if (args.length > 0) {
            BenchmarkApplication.bench(args);
        } else {
            CliUiApplication.run();
        }
    }
}
