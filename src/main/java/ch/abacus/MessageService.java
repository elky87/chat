/*
 * Creator:
 * 09.06.22 12:11 christophellinger
 *
 * Maintainer:
 * 09.06.22 12:11 christophellinger
 *
 * Last Modification:
 * $Id:$
 *
 * Copyright (c) 2022 ABACUS Research AG, All Rights Reserved
 */
package ch.abacus;

import ch.abacus.data.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MessageService {

    private MessageRepository messageRepository;
    private EncryptionService encryptionService;

    @Autowired
    public MessageService(final MessageRepository messageRepository, final EncryptionService encryptionService) {
        this.messageRepository = messageRepository;
        this.encryptionService = encryptionService;
    }

    List<Message> getMessageCountInMap() { // primary for testing
        return messageRepository.getAllMessages();
    }

    public Message generateMessage(String unencryptedMessage, String password, Duration duration, boolean selfDestructAfterRead) throws Exception {
        final String encryptedMessage = encryptionService.encrypt(unencryptedMessage, password);
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
}
