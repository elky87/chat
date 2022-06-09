/*
 * Creator:
 * 09.06.22 10:16 christophellinger
 *
 * Maintainer:
 * 09.06.22 10:16 christophellinger
 *
 * Last Modification:
 * $Id:$
 *
 * Copyright (c) 2022 ABACUS Research AG, All Rights Reserved
 */
package ch.abacus;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Duration;
import java.util.Optional;

import ch.abacus.data.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EncryptionServiceTest {

    @Autowired
    EncryptionService encryptionService;

    @Test
    void encrypt_decrypt() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
                                  BadPaddingException, InvalidKeySpecException, InvalidKeyException {

        String password = "secret";
        String originalMessage = "Geheime Nachricht";

        String encryptedMessage = encryptionService.encrypt(originalMessage, password);
        String decryptedMessage = encryptionService.decrypt(encryptedMessage, password);

        Assertions.assertEquals(originalMessage, decryptedMessage);
    }

    @Test
    void generateMessage() throws Exception {
        Assertions.assertEquals(0, encryptionService.getMessageCountInMap().size());
        final Message message = encryptionService.generateMessage("geheim", "password", Duration.ofDays(1), true);
        Assertions.assertEquals(encryptionService.encrypt("geheim", "password"), message.getContent());
        Assertions.assertEquals(Duration.ofDays(1), message.getSelfDestruct());
        Assertions.assertTrue(message.isSelfDestructAfterRead());
        Assertions.assertEquals(1, encryptionService.getMessageCountInMap().size());

        final Optional<Message> sameMessage = encryptionService.getMessage(encryptionService.getMessageCountInMap().stream().findFirst().get().getId());
        Assertions.assertEquals(0, encryptionService.getMessageCountInMap().size());
    }

}