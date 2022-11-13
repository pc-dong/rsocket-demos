package cn.dpc;

import io.netty.handler.codec.json.JsonObjectDecoder;
import io.rsocket.lease.Lease;
import io.rsocket.lease.LeaseSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.codec.StringDecoder;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.util.MimeType;
import org.springframework.web.util.pattern.PathPatternRouteMatcher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Configuration
public class ServerConfig {

    @Value("${rsocket.port: 7001}")
    private Integer port;

    @Bean
    public RSocketRequester requester(RSocketMessageHandler handler) {
        return RSocketRequester.builder()
                .rsocketStrategies(handler.getRSocketStrategies())
//                .dataMimeType(MediaType.APPLICATION_CBOR)
                .rsocketConnector(connector -> connector.acceptor(handler.responder())
//                        .lease(leaseSpec -> leaseSpec.maxPendingRequests(20)
//                        .sender(() -> Flux.interval(Duration.ofSeconds(0), Duration.ofMinutes(2))
//                                .map(i -> Lease.create(Duration.ofMinutes(2), 10))))
                )
                .tcp("localhost", port);
    }
}
