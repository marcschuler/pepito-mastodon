package de.marcschuler.pepitomastodon;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PepitoApiUtilTest {

    @Test
    void testTimeConversion(){
        assertEquals(LocalDateTime.of(2024,9,8,17,11,5),PepitoApiUtil.unixToLocalDate(1725714568L));
    }

}