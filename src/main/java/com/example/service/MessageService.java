package com.example.service;

import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import com.example.entity.Account;
import com.example.entity.Message;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message createMessage(Message message) {
        int postedBy = message.getPostedBy();
        if(!accountRepository.existsById(postedBy)) {
            throw new IllegalArgumentException("User does not exist in the database");
        }
        return messageRepository.save(message);
    }

    public List<Message> getMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> findMessageById(int messageId) {
        return messageRepository.findById(messageId);
    }

    public int deleteMessageById(int messageId) {
        Optional<Message> message = messageRepository.findById(messageId);
        if (message.isPresent()) {
            messageRepository.delete(message.get());
            return 1;
        } else {
            return 0;
        }
    }

    public int updateMessageById(int messageid, String messageText) {
        Optional<Message> message = messageRepository.findById(messageid);
        if (message.isPresent()) {
            Message updatedMessage = message.get();
            updatedMessage.setMessageText(messageText);
            messageRepository.save(updatedMessage);
            return 1;
        } else {
            return 0;
        }
    }
}
