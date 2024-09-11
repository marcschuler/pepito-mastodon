package de.marcschuler.pepitomastodon.api;

/**
 * The event type.
 * A heartbeat will occur every 10 seconds to keep the connection alive
 * A pepito is a cat event, the cat either enters or leaves the house
 */
public enum PepitoApiEvent {
    HEARTBEAT,
    PEPITO
}
