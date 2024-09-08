package de.marcschuler.pepitomastodon.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.marcschuler.pepitomastodon.PepitoApiUtil;
import de.marcschuler.pepitomastodon.api.PepitoApiEvent;
import de.marcschuler.pepitomastodon.api.PepitoApiResponse;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.sse.InboundSseEvent;
import jakarta.ws.rs.sse.SseEventSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.ObjectMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import social.bigbone.api.exception.BigBoneRequestException;

import java.io.IOException;
import java.lang.runtime.ObjectMethods;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class PepitoApiService {

    private final MastodonService mastodonService;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final ObjectMapper mapper = new ObjectMapper()
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);

    @Value("${pepito-api.url}")
    private String url;

    @PostConstruct
    void init() {
        executorService.scheduleWithFixedDelay(() -> {
            try {
                startApiListener();
            } catch (Exception e) {
                log.error("Error with API connection", e);
            }
        }, 1, 30, TimeUnit.SECONDS);
    }

    public void startApiListener() throws InterruptedException {
        log.info("Starting API listener");
        var target = ClientBuilder.newClient()
                .target(this.url);
        try (var source = SseEventSource.target(target).build()) {
            source.register(event -> {
                try {
                    onApiEvent(event);
                } catch (Exception e) {
                    log.error("Could not process event from API", e);
                }
            });
            source.open();
            while(source.isOpen()){
                Thread.sleep(1000);
            }
        }
    }

    public void onApiEvent(InboundSseEvent event) throws IOException, BigBoneRequestException {
        var dataString = event.readData(String.class);
        var data = mapper.readValue(dataString, PepitoApiResponse.class);
        log.info("Response from API: {}", mapper.writeValueAsString(data));
        if (data.getEvent() == PepitoApiEvent.HEARTBEAT) {
            log.info("Received heartbeat at {}", PepitoApiUtil.unixToLocalDate(data.getTime()));
            return;
        }
        try {
            mastodonService.sendPost(data);
        } catch (BigBoneRequestException e) {
            log.error("Could not send post to mastodon", e);
            throw new RuntimeException();
        }
    }
}
