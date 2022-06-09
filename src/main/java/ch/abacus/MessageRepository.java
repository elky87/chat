/*
 * Creator:
 * 09.06.22 10:52 christophellinger
 *
 * Maintainer:
 * 09.06.22 10:52 christophellinger
 *
 * Last Modification:
 * $Id:$
 *
 * Copyright (c) 2022 ABACUS Research AG, All Rights Reserved
 */
package ch.abacus;

import ch.abacus.data.Message;

import java.util.*;

public class MessageRepository {

    private static final MessageRepository INSTANCE = new MessageRepository();

    public static MessageRepository getInstance() {
        return INSTANCE;
    }

    private MessageRepository() {
    }

    private final Map<UUID, Message> messages = new HashMap<>();

    public Optional<Message> getMessage(UUID uuid) {
        return Optional.ofNullable(messages.get(uuid));
    }

    public void addMessage(Message message) {
        messages.put(message.getId(), message);
    }

    public void deleteMessage(UUID uuid) {
        messages.remove(uuid);
    }

    public List<Message> getAllMessages() {
        return new ArrayList<>(messages.values());
    }

}
