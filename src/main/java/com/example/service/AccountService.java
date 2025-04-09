package com.example.service;

import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import com.example.entity.Account;
import com.example.entity.Message;

import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final MessageRepository messageRepository;

    public AccountService(AccountRepository accountRepository, MessageRepository messageRepository) {
        this.accountRepository = accountRepository;
        this.messageRepository = messageRepository;
    }

    public Optional<Account> register(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();

        if (username == null || username.trim().isEmpty() || password == null || password.length() < 4) {
            return Optional.empty();
        }

        if (accountRepository.findByUsername(username).isPresent()) {
            return Optional.of(new Account(-1, null, null));
        }

        Account savedAccount = accountRepository.save(account);
        return Optional.of(savedAccount);
    }

    public Optional<Account> login(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();
        Optional<Account> existingAccount = accountRepository.findByUsername(username);

        if (existingAccount.isPresent() && existingAccount.get().getPassword().equals(password)) {
            return existingAccount;
        } else {
            return Optional.empty();
        }  
    }

    public List<Message> getAllMessagesByAccountId(int accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}
