package ru.pastebin.cli;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.pastebin.cli.dto.Paste;
import ru.pastebin.cli.service.KafkaProducer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class MainApplication {
    private static NewTopic kafkaSendPasteTopic;
    private static KafkaProducer kafkaProducer;

    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext("ru.pastebin.cli");
        kafkaProducer = ctx.getBean(KafkaProducer.class);
        kafkaSendPasteTopic = ctx.getBean(NewTopic.class);

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
                switch (type) {
                     case 'c':
                        String paste = query.substring(1).trim();
                        Date date = new Date();
                        createPaste(new Paste(paste, date));
                        break;
                    case 'g':
                        String id = query.substring(1).trim();
                        getPaste(id);
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

    private static void createPaste(Paste paste) {
        kafkaProducer.sendCreatePasteMessage(kafkaSendPasteTopic.name(), paste);
    }

    private static Paste getPaste(String id) {
        return null;
    }
}
