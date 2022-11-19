package cn.dpc.controller;

import cn.dpc.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class RSocketService {
    Flux<Character> splitString(String payload) {
        return Flux.interval(Duration.ofMillis(20))
                .map(index -> payload.charAt(index.intValue()))
                .take(payload.length())
                .doOnNext(System.out::println);
    }

    Mono<Message> toUpperCase(String payload,
                              String clientId) {
        return Mono.just(payload.toUpperCase())
                .map(Message::new)
                .log("toUpperCase " + clientId);
    }
}
