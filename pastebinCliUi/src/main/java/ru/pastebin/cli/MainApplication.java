package ru.pastebin.cli;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import ru.pastebin.cli.beans.KafkaProducerConfig;
import ru.pastebin.cli.beans.KafkaTopic;
import ru.pastebin.cli.model.Paste;
import ru.pastebin.cli.service.KafkaPasteProducer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class MainApplication {
    private static NewTopic kafkaSendPasteTopic;
    private static KafkaPasteProducer kafkaPasteProducer;

    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext("ru.pastebin.cli");
        kafkaPasteProducer = ctx.getBean(KafkaPasteProducer.class);
        kafkaSendPasteTopic = ctx.getBean(NewTopic.class);

        System.out.println("c = create, requires text.\ng = get, requires id.\ne = exit.");

        boolean noExit = true;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in))) {
            while (noExit) {
                String query = reader.readLine();
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
        kafkaPasteProducer.sendMessage(kafkaSendPasteTopic.name(), paste);
    }

    private static Paste getPaste(String id) {
        return null;
    }
}
