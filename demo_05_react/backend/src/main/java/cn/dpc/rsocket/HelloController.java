package cn.dpc.rsocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Controller
@Slf4j
public class HelloController {

    @MessageMapping("reverse")
    public Mono<String> reverse(@Payload String data) {

        StringBuilder result = new StringBuilder();

        // append a string into StringBuilder input1
        result.append(data);

        // reverse StringBuilder input1
        result.reverse();

        // print reversed String
        return Mono.just(result.toString());
    }

    @MessageMapping("toUpperCase")
    public Mono<String> toUpperCase(@Payload String data) {
        return Mono.just(data.toUpperCase());
    }

    @MessageMapping("split")
    public Flux<String> split(@Payload String data) {
        return Flux.interval(Duration.ofMillis(100))
                .map(index -> String.valueOf(data.charAt(index.intValue())))
                .take(data.length())
                .doOnNext(System.out::println);
    }

    @MessageMapping("log")
    public Mono<Void> log(@Payload String data) {
        log.info("received: " + data);
        return Mono.empty();
    }

    @MessageMapping("channelToUpperCase")
    public Flux<String> toUpperCase(@Payload Flux<String> data) {
        return data.map(item -> item.toUpperCase());
    }
}
