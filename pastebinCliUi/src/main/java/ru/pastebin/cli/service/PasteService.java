package ru.pastebin.cli.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.pastebin.cli.dto.Paste;

@Service
@PropertySource("classpath:application.properties")
public class PasteService {
    private final String pasteMicroserviceUrl;
    private final String createPasteEndpoint;
    private final ObjectMapper objectMapper;

    public PasteService(
            @Value("${api.paste-microservice-url}") String pasteMicroserviceUrl,
            @Value("${api.create_paste_endpoint}") String createPasteEndpoint
    ) {
        this.pasteMicroserviceUrl = pasteMicroserviceUrl;
        this.createPasteEndpoint = createPasteEndpoint;
        this.objectMapper = new ObjectMapper();
    }

    public void sendPaste(Paste paste) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String pasteJson;
        try {
            pasteJson = objectMapper.writeValueAsString(paste);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(pasteMicroserviceUrl + "/" + createPasteEndpoint);

        HttpEntity<?> entity = new HttpEntity<>(pasteJson, headers);

        restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.PUT,
                entity,
                String.class
        );
    }
}
