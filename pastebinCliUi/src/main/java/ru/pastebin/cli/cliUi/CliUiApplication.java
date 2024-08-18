package ru.pastebin.cli.cliUi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import ru.pastebin.cli.dto.Paste;
import ru.pastebin.cli.dto.PasteWithHash;
import ru.pastebin.cli.service.PasteService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

@Slf4j
@Component
public class CliUiApplication {
    private PasteService pasteService;

    public void run() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext("ru.pastebin.cli");
        pasteService = ctx.getBean(PasteService.class);

        System.out.println("c = create, requires text.\ng = get, requires id.\ne = exit.");

        boolean noExit = true;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in))) {
            while (noExit) {
                String query = reader.readLine();
                if (query.isBlank()) {
                    System.out.println("Wrong query, try again.");
                    continue;
                }
                char type = query.charAt(0);
                String queryParam = query.substring(1).trim();
                if (queryParam.isEmpty()) {
                    System.out.println("Query param is empty, try again.");
                    continue;
                }
                switch (type) {
                    case 'c':
                        long date = System.currentTimeMillis();
                        System.out.println(createPaste(new Paste(queryParam, date)));
                        break;
                    case 'g':
                        PasteWithHash responsePaste = getPaste(queryParam);
                        System.out.println(responsePaste);
                        break;
                    case 'e':
                        noExit = false;
                        break;
                    default:
                        System.out.println("Wrong query, try again.");
                        break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private PasteWithHash createPaste(Paste paste) {
        return pasteService.sendPaste(paste);
    }

    private PasteWithHash getPaste(String id) {
        try {
            return pasteService.getPaste(id);
        } catch (HttpClientErrorException e) {
            log.warn("Paste not found: {}", id);
            return null;
        }
    }
}
