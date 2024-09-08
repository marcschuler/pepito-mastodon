package de.marcschuler.pepitomastodon.service;

import de.marcschuler.pepitomastodon.PepitoApiUtil;
import de.marcschuler.pepitomastodon.api.PepitoApiResponse;
import de.marcschuler.pepitomastodon.api.PepitoApiType;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import social.bigbone.MastodonClient;
import social.bigbone.api.entity.MediaAttachment;
import social.bigbone.api.exception.BigBoneRequestException;
import social.bigbone.api.method.FileAsMediaAttachment;

import java.io.IOException;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class MastodonService {

    @Value("${mastodon.instance}")
    private String instance;
    @Value("${mastodon.token}")
    private String token;

    private MastodonClient mastodonClient;

    @PostConstruct
    void init() {
        log.info("Initialising Mastodon client on {}", this.instance);
        this.mastodonClient = new MastodonClient.Builder(this.instance)
                .accessToken(this.token)
                .build();
    }

    public void sendPost(PepitoApiResponse response) throws BigBoneRequestException, IOException {
        log.info("Creating mastodon post");
        var imageResponse = ClientBuilder.newClient()
                .target(response.getImg())
                .request().get();
        var image = imageResponse.readEntity(byte[].class);
        var imageMediaType = "image/jpeg";
        var mastodonAttachment = uploadAttachment(image, imageMediaType);

        var postText = "Pépito is " +
                (response.getType() == PepitoApiType.IN ? "back home" : "out")
                + "(" + PepitoApiUtil.unixToLocalDate(response.getTime()).toLocalTime().format(DateTimeFormatter.ISO_LOCAL_TIME)+ ")";

        var post = this.mastodonClient.statuses()
                .postStatus(postText, List.of(mastodonAttachment.getId()))
                .execute();
        log.info("Created post with ID {}/{}", post.getAccount(), post.getId());

    }

    public MediaAttachment uploadAttachment(byte[] attachment, String mediaType) throws BigBoneRequestException, IOException {
        //TODO BigBone only accepts files as input. This is terrible and should be reworked, maybe I read the docs wrong
        var path = Files.createTempFile("petito-mastodon", UUID.randomUUID().toString());
        log.info("Writing image file to {}", path.toAbsolutePath());
        Files.write(path, attachment);

        var response = this.mastodonClient.media()
                .uploadMediaAsync(new FileAsMediaAttachment(path.toFile(), mediaType)).execute();
        Files.delete(path);
        return response;
    }
}
