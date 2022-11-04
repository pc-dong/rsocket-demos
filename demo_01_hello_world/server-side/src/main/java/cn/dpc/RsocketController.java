package cn.dpc;

import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

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
}
