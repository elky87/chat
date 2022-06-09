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

import java.util.List;
import java.util.stream.Collectors;

import ch.abacus.data.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class MessageDeleteScheduler implements Runnable {

    private final MessageRepository repo;

    @Autowired
    public MessageDeleteScheduler(MessageRepository repo) {
        this.repo = repo;
    }

    @Scheduled(fixedRate = 10000)
    public void cleanMessages() {
        List<Message> toBeDeletedMessages = repo.getAllMessages().stream()
                                                .filter(Message::isDurationOver)
                                                .collect(Collectors.toList());

        for (Message toBeDeletedMessage : toBeDeletedMessages) {
            repo.deleteMessage(toBeDeletedMessage.getId());
        }
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
