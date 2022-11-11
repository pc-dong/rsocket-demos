package cn.dpc;

import io.rsocket.util.DefaultPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rsocket.server.LocalRSocketServerPort;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static cn.dpc.Constants.CLIENT_ID_MIME;

@SpringBootTest(properties = "spring.rsocket.server.port=0")
class RsocketControllerTest {

    @LocalRSocketServerPort
    int port;

    @Autowired
    RSocketRequester.Builder builder;

    RSocketRequester requester;

    @BeforeEach
    public void init() {
        requester = builder.tcp("127.0.0.1", port);
    }

    @Test
    public void should_toUpperCase_success() {
        requester.route("toUpperCase")
                .metadata("1111", CLIENT_ID_MIME)
                .data(Mono.just("hello"), String.class)
                .retrieveMono(Message.class)
                .as(StepVerifier::create)
                .expectNextMatches(message -> message.getMessage().equals("HELLO"))
                .verifyComplete();
    }

}