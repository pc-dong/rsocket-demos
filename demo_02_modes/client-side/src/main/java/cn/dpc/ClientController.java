package cn.dpc;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ClientController {
    private final RSocketRequester requester;

    @GetMapping("connect")
    void connect() {
        requester.rsocketClient().connect();
    }

    @GetMapping("toUpperCase")
    Mono<Message> toUpperCase(String message) {
        return requester.route("toUpperCase")
                .metadata(metadataSpec -> metadataSpec.metadata(UUID.randomUUID().toString(), MimeType.valueOf("message/x.client.id")))
                .data(message)
                .retrieveMono(Message.class);
    }

    @GetMapping("splitString")
    Mono<List<String>> splitString(String message) {
        return requester.route("splitString")
                .data(message)
                .retrieveFlux(Character.class)
                .map(c -> c.toString())
                .doOnNext(System.out::println)
                .collectList();

    }

    @PostMapping("channelToUpperCase")
    Flux<Message> channelToUpperCase(@RequestBody List<String> messages) {
        return requester.route("channelToUpperCase")
                .data(Flux.fromIterable(messages), String.class)
                .retrieveFlux(Message.class)
                .doOnNext(System.out::println);
    }

    @GetMapping("log")
    Mono<Void> log(String message) {
        return requester.route("log")
                .data(message)
                .retrieveMono(Void.class);
    }

}
