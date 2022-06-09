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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MessageRepository {

    private Map<UUID, Message> messages = new HashMap<>();

    public Optional<Message> getMessage(UUID uuid){
        return null;
    }

    public void addMessage(Message message) {

    }

    public void deleteMessage(UUID uuid) {

    }

}
