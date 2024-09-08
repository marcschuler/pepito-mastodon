package de.marcschuler.pepitomastodon;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.TimeZone;

public class PepitoApiUtil {

    public static LocalDateTime unixToLocalDate(long unix) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(unix),
                TimeZone.getTimeZone("Europe/Madrid").toZoneId());
    }
}
