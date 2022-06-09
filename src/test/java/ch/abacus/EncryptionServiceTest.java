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

import ch.abacus.data.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EncryptionServiceTest {

    @Test
    void encrypt_decrypt() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {

        String password = "secret";
        String originalMessage = "Geheime Nachricht";

        EncryptionService encryptionService = new EncryptionService();

        String encryptedMessage = encryptionService.encrypt(originalMessage, password);
        String decryptedMessage = encryptionService.decrypt(encryptedMessage, password);

        Assertions.assertEquals(originalMessage, decryptedMessage);
    }

    @Test
    void generateMessage() throws Exception {
        final EncryptionService encryptionService = new EncryptionService();
        final Message message = encryptionService.generateMessage("geheim", "password", Duration.ZERO, false);
        Assertions.assertEquals(encryptionService.encrypt("geheim", "password"), message.getContent());
        Assertions.assertEquals(Duration.ZERO, message.getSelfDestruct());
        Assertions.assertFalse(message.isSelfDestructAfterRead());
    }

}