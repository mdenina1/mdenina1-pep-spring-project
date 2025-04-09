package com.example.controller;

import com.example.entity.Account;
import com.example.service.AccountService;
import com.example.entity.Message;
import com.example.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    private final AccountService accountService;
    private final MessageService messageService;

    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        Optional<Account> result = accountService.register(account);

        if (result.isEmpty()) {
            return ResponseEntity.badRequest().build();
        } else if (result.get().getAccountId() == -1) {
            return ResponseEntity.status(409).build();
        } else {
            return ResponseEntity.ok(result.get());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Optional<Account> result = accountService.login(account);
        
        if (result.isEmpty()) {
            return ResponseEntity.status(401).build();
        } else {
            return ResponseEntity.ok(result.get());
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        try {
            if (message.getMessageText() == null || message.getMessageText().trim().isEmpty() || message.getMessageText().length() > 255) {
                return ResponseEntity.badRequest().build();
            }
            Message createdMessage = messageService.createMessage(message);
            return ResponseEntity.ok(createdMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }  
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getMessages() {
        List<Message> messages = messageService.getMessages();
        if (messages.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable("messageId") int messageId) {
        Optional<Message> message = messageService.findMessageById(messageId);
        if(message.isPresent()) {
            return ResponseEntity.ok(message.get());
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable("messageId") int messageId) {
        int updatedRows = messageService.deleteMessageById(messageId);
        if(updatedRows > 0) {
            return ResponseEntity.ok(updatedRows);
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessageById(@PathVariable("messageId") int messageId, @RequestBody Message message) {
        try {
            if (message.getMessageText() == null || message.getMessageText().trim().isEmpty() || message.getMessageText().length() > 255) {
                return ResponseEntity.badRequest().build();
            }
            int updatedRows = messageService.updateMessageById(messageId, message.getMessageText());
            if (updatedRows > 0) {
                return ResponseEntity.ok(updatedRows);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }  
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable("accountId") int accountId) {
        List<Message> messages = accountService.getAllMessagesByAccountId(accountId);
        if (messages.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(messages);
    }
}
