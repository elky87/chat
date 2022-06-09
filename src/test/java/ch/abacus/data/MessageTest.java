/*
 * Creator:
 * 09.06.22 11:10 christophellinger
 *
 * Maintainer:
 * 09.06.22 11:10 christophellinger
 *
 * Last Modification:
 * $Id:$
 *
 * Copyright (c) 2022 ABACUS Research AG, All Rights Reserved
 */
package ch.abacus.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    void isDurationOver() {
        Message message = new Message();
        message.setTimestamp(Instant.now().minus(1, ChronoUnit.MINUTES));
        message.setSelfDestruct(Duration.of(5, ChronoUnit.MINUTES));

        Assertions.assertFalse(message.isDurationOver());

        message.setSelfDestruct(Duration.of(1, ChronoUnit.SECONDS));

        Assertions.assertTrue(message.isDurationOver());
    }
}