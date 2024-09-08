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

import java.util.List;

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

    public void sendPost(PepitoApiResponse response) throws BigBoneRequestException {
        log.info("Creating mastodon post");
        var imageResponse = ClientBuilder.newClient()
                .target(response.getImg())
                .request().get();
        var image = imageResponse.readEntity(byte[].class);
        var imageMediaType = "image/jpeg";
        var mastodonAttachment = uploadAttachment(image, imageMediaType);

        var postText = "Pépito is " +
                (response.getType() == PepitoApiType.IN ? "back home" : "out")
                + "(" + PepitoApiUtil.unixToLocalDate(response.getTime()).toLocalTime().format(null) + ")";

        var post = this.mastodonClient.statuses()
                .postStatus(postText, List.of(mastodonAttachment.getId()))
                .execute();
        log.info("Created post with ID {}/{}", post.getAccount(), post.getId());

    }

    public MediaAttachment uploadAttachment(byte[] attachment, String mediaType) throws BigBoneRequestException {
        return this.mastodonClient.media()
                .uploadMediaAsync(new FileAsMediaAttachment(null, mediaType)).execute();
    }
}
