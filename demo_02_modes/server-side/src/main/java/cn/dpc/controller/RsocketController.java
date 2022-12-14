package cn.dpc.controller;

import cn.dpc.Message;
import cn.dpc.StatusReport;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

import static cn.dpc.config.Constants.CLIENT_ID;

@Controller
@Log4j2
@RequiredArgsConstructor
public class RsocketController {
    private final RSocketService rSocketService;
    @ConnectMapping
    Mono<Void> setup(RSocketRequester requester) {
        log.info("connected");
        requester
                .route("status")
                .data(Mono.just("5"))
                .retrieveFlux(StatusReport.class)
                .subscribe(bar -> {
                    log.info(bar);
                });

        return Mono.empty().log("connected")
                .then();
    }

    @MessageMapping("toUpperCase")
    Mono<Message> toUpperCase(@Payload String payload,
                              @Header(CLIENT_ID) String clientId) {
        return rSocketService.toUpperCase(payload, clientId);
    }

    @MessageMapping("repeatToUpperCase")
    Flux<Message> repeatToUpperCase(@Payload String payload) {
        AtomicLong counter = new AtomicLong(0L);
        return Mono.fromCallable(() -> counter.getAndIncrement() + "\t" + payload.toUpperCase())
                .repeat()
                .map(Message::new)
                .log("repeatToUpperCase");
    }


    @MessageMapping("splitString")
    Flux<Character> splitString(@Payload String payload) {
        return rSocketService.splitString(payload);
    }

    @MessageMapping({"channelToUpperCase"})
    Flux<Message> channelToUpperCase(Flux<String> messages) {
        return Flux.interval(Duration.ofSeconds(1)).zipWith(messages)
                .map(tuple -> tuple.getT2().toUpperCase())
                .map(Message::new);
    }

    @MessageMapping("log")
    void log(final String message) {
        log.info("receive log message: {}", message);
    }
}
