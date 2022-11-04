package cn.dpc;

import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;

@Controller
@Log4j2
public class RsocketController {

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

        return Mono.empty();
    }


    @MessageMapping("toUpperCase")
    Mono<String> toUpperCase(@Payload String payload) {
        return Mono.just(payload.toUpperCase());
    }


    @MessageMapping("splitString")
    Flux<String> splitString(@Payload String payload) {
        return Flux.interval(Duration.ofSeconds(1))
                .map(index -> payload.charAt(index.intValue()))
                .take(payload.length())
                .map(String::valueOf)
                .doOnNext(System.out::println);
    }

    @MessageMapping({"channelToUpperCase"})
    Flux<String> channelToUpperCase(Flux<String> messages) {
        return Flux.interval(Duration.ofSeconds(1)).zipWith(messages)
                .map(tuple -> tuple.getT2().toUpperCase());
    }

    @MessageMapping("log")
    void log(final String message) {
        log.info("receive log message: {}", message);
    }
}
