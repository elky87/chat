/*
 * Creator:
 * 09.06.22 10:55 christophellinger
 *
 * Maintainer:
 * 09.06.22 10:55 christophellinger
 *
 * Last Modification:
 * $Id:$
 *
 * Copyright (c) 2022 ABACUS Research AG, All Rights Reserved
 */
package ch.abacus;

import ch.abacus.data.Message;

import java.util.List;
import java.util.stream.Collectors;

public class MessageDeleteScheduler implements Runnable {

    private final MessageRepository repo;

    public MessageDeleteScheduler(MessageRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run() {

        List<Message> toBeDeletedMessages = repo.getAllMessages().stream()
                .filter(Message::isDurationOver)
                .collect(Collectors.toList());

        for (Message toBeDeletedMessage : toBeDeletedMessages) {
            repo.deleteMessage(toBeDeletedMessage.getId());
        }

    }
}
