package de.marcschuler.pepitomastodon.api;

import lombok.Data;

@Data
public class PepitoApiResponse {
    private PepitoApiEvent event;
    private long time;
    private PepitoApiType type;
    private String img;
}
