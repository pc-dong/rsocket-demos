package cn.dpc.controller;

import cn.dpc.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

import javax.annotation.security.PermitAll;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @GetMapping("uc")
    @PermitAll
    Mono<Message> toUpperCaseWeb(@Header("uc") String clientId, String message) {
        return Mono.just(message.toUpperCase())
                .map(Message::new)
                .log("toUpperCase " + clientId);
    }
}
