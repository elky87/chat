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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

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

}