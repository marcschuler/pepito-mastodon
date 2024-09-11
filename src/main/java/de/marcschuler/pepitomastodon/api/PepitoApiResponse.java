package de.marcschuler.pepitomastodon.api;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * The Response object on every event
 */
@Data
@NoArgsConstructor
public class PepitoApiResponse {
    // Event type, either a heartbeat (every 10s) or a cat evenbt
    @NotNull
    private PepitoApiEvent event;
    // Unix Timestamp
    private long time;
    // Is null on heartbeat, not null on a cat event
    private PepitoApiType type;
    // Is null on heartbeat, not null on a cat event
    private String img;
}
