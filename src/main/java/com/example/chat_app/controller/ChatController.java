package com.example.chat_app.controller;

import com.example.chat_app.dto.Chat;
import com.example.chat_app.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatRepository chatRepository;

    @GetMapping(value = "/sender/{sender}/receiver/{receiver}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public Flux<Chat> getMsg(@PathVariable String sender, @PathVariable String receiver) {

        return chatRepository.mFindBySender(sender, receiver).subscribeOn(Schedulers.boundedElastic());

    }

    @PostMapping("/chat")
    @ResponseBody
    public Mono<Chat> setMsg(@RequestBody Chat chat) {

        chat.setCreatedAt(LocalDateTime.now());
        return chatRepository.save(chat);

    }

}
