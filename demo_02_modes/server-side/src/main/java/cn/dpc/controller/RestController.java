package cn.dpc.controller;

import cn.dpc.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@org.springframework.web.bind.annotation.RestController
@RequiredArgsConstructor
@Slf4j
public class RestController {
    private final RSocketService rSocketService;

    @GetMapping("uc")
    Mono<Message> toUpperCaseWeb(@Header("uc") String clientId, String message) {
        return rSocketService.toUpperCase(message, clientId);
    }

    @GetMapping("sl")
    Flux<String> splitString(String message) {
        return rSocketService.splitString(message)
                .map(String::valueOf);
    }

    @GetMapping("log")
    void log(String message) {
        log.info("receive log message: {}", message);
    }
}
