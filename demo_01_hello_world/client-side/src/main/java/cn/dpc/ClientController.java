package cn.dpc;

import cn.dpc.StatusReport;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
@Log4j2
public class ClientController {

    @MessageMapping("status")
    public Flux<StatusReport> status(String data) {
        log.info("receive data: " + data);
        return Flux.just(new StatusReport("123"), new StatusReport("2324324"))
                .doOnNext(chs -> log.info("Sending status " + chs.getStatus()));
    }

}
