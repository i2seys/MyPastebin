package ru.pastebin.cli.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.pastebin.cli.dto.Paste;
import ru.pastebin.cli.dto.PasteWithHash;

@Service
@PropertySource("classpath:application.properties")
public class PasteService {
    private final String pasteMicroserviceUrl;
    private final String createPasteEndpoint;
    private final String getPasteEndpoint;
    private final ObjectMapper objectMapper;

    public PasteService(
            @Value("${api.paste-microservice-url}") String pasteMicroserviceUrl,
            @Value("${api.create_paste_endpoint}") String createPasteEndpoint,
            @Value("${api.get_paste_endpoint}") String getPasteEndpoint
    ) {
        this.pasteMicroserviceUrl = pasteMicroserviceUrl;
        this.createPasteEndpoint = createPasteEndpoint;
        this.getPasteEndpoint = getPasteEndpoint;
        this.objectMapper = new ObjectMapper();
    }

    public PasteWithHash sendPaste(Paste paste) {
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

        ResponseEntity<?> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.PUT,
                entity,
                PasteWithHash.class
        );
        return getResponse(response);
    }

    public PasteWithHash getPaste(String id) throws HttpClientErrorException {
        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                pasteMicroserviceUrl + "/" + getPasteEndpoint + "/" + id
        );

        ResponseEntity<?> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                PasteWithHash.class
        );
        return getResponse(response);
    }

    private PasteWithHash getResponse(ResponseEntity<?> response) {
        if (response.getStatusCode() == HttpStatus.OK) {
            return (PasteWithHash) response.getBody();
        } else {
            throw new HttpClientErrorException(response.getStatusCode(), String.valueOf(response.getBody()));
        }
    }
}
