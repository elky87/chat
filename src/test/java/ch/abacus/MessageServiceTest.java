/*
 * Creator:
 * 09.06.22 12:14 christophellinger
 *
 * Maintainer:
 * 09.06.22 12:14 christophellinger
 *
 * Last Modification:
 * $Id:$
 *
 * Copyright (c) 2022 ABACUS Research AG, All Rights Reserved
 */
package ch.abacus;

import ch.abacus.data.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.Optional;

@SpringBootTest
class MessageServiceTest {

    @Autowired
    MessageService messageService;

    @Autowired
    EncryptionService encryptionService;

    @Test
    void generateMessage() throws Exception {
        Assertions.assertEquals(0, messageService.getMessageCountInMap().size());
        final Message message = messageService.generateMessage("geheim", "password", Duration.ofDays(1), true);
        Assertions.assertEquals(encryptionService.encrypt("geheim", "password"), message.getContent());
        Assertions.assertEquals(Duration.ofDays(1), message.getSelfDestruct());
        Assertions.assertTrue(message.isSelfDestructAfterRead());
        Assertions.assertEquals(1, messageService.getMessageCountInMap().size());

        final Optional<Message> sameMessage = messageService.getMessage(messageService.getMessageCountInMap().stream().findFirst().get().getId());
        Assertions.assertEquals(0, messageService.getMessageCountInMap().size());
    }

}