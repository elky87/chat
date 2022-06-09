/*
 * Creator:
 * 09.06.22 09:22 christophellinger
 *
 * Maintainer:
 * 09.06.22 09:22 christophellinger
 *
 * Last Modification:
 * $Id:$
 *
 * Copyright (c) 2022 ABACUS Research AG, All Rights Reserved
 */
package ch.abacus;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import ch.abacus.data.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    private final static String SALT = "alsdkfqthlehbkjvh83qljr51234outr18gh1hg1g";

    private MessageRepository messageRepository;

    @Autowired
    public EncryptionService(final MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message generateMessage(String unencryptedMessage, String password, Duration duration, boolean selfDestructAfterRead) throws Exception {
        final String encryptedMessage = encrypt(unencryptedMessage, password);
        final Message message = new Message();
        message.setId(UUID.randomUUID());
        message.setContent(encryptedMessage);
        message.setTimestamp(Instant.now());
        message.setSelfDestruct(duration);
        message.setSelfDestructAfterRead(selfDestructAfterRead);
        messageRepository.addMessage(message);
        return message;
    }

    public Optional<Message> getMessage(UUID uuid) {
        final Optional<Message> message = messageRepository.getMessage(uuid);

        if (message.isPresent() && message.get().isDurationOver()) {
            messageRepository.deleteMessage(message.get().getId());
            return Optional.empty();
        }
        if (message.isPresent() && (message.get().isSelfDestructAfterRead())) {
            messageRepository.deleteMessage(uuid);
        }
        return message;
    }

    public String encrypt(String input, String password) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, getKeyFromPassword(password), generateIv());
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }

    public String decrypt(String cipherText, String password) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, getKeyFromPassword(password), generateIv());
        byte[] plainText = cipher.doFinal(Base64.getDecoder()
                .decode(cipherText));
        return new String(plainText);
    }

    private SecretKey getKeyFromPassword(String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), SALT.getBytes(), 65536, 256);
        return new SecretKeySpec(factory.generateSecret(spec)
                .getEncoded(), "AES");
    }

    private IvParameterSpec generateIv() {
        return null; // should be changed in the future
    }
}
