package de.marcschuler.pepitomastodon;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class PepitoApiUtil {

    public static LocalDateTime unixToLocalDate(long unix){
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(unix),
                TimeZone.getDefault().toZoneId());
    }
}
