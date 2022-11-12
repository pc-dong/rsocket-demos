package cn.dpc;

import io.rsocket.core.Resume;
import io.rsocket.frame.LeaseFrameCodec;
import io.rsocket.lease.Lease;
import io.rsocket.lease.LeaseSender;
import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer;
import org.springframework.boot.rsocket.server.RSocketServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static cn.dpc.Constants.*;

@Configuration
public class ServerConfig {

    @Bean
    public RSocketStrategiesCustomizer strategiesCustomizer() {
        return strategies -> strategies
                .metadataExtractorRegistry(registry -> registry.metadataToExtract(CLIENT_ID_MIME, String.class, CLIENT_ID))
                .metadataExtractorRegistry(registry -> registry.metadataToExtract(METHOD_MIME, String.class, METHOD));
    }

    @Bean
    public RSocketServerCustomizer serverCustomizer() {
        return rSocketServer -> {
            rSocketServer.resume(new Resume());
            rSocketServer.lease(leaseSpec -> leaseSpec.maxPendingRequests(256)
                    .sender(() -> Flux.interval(Duration.ofSeconds(0), Duration.ofMinutes(2))
                            .map(i -> Lease.create(Duration.ofMinutes(2), 10))));
        };
    }
}
