package cn.dpc.config;

import io.rsocket.core.Resume;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.rsocket.RSocketMessageHandlerCustomizer;
import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer;
import org.springframework.boot.rsocket.server.RSocketServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver;
import org.springframework.security.rsocket.metadata.BasicAuthenticationEncoder;
import org.springframework.security.rsocket.metadata.SimpleAuthenticationEncoder;


import static cn.dpc.config.Constants.*;

@Configuration
public class ServerConfig {

    @Bean
    public RSocketStrategiesCustomizer strategiesCustomizer() {
        return strategies -> strategies
                .metadataExtractorRegistry(registry -> registry.metadataToExtract(CLIENT_ID_MIME, String.class, CLIENT_ID))
                .metadataExtractorRegistry(registry -> registry.metadataToExtract(FILE_NAME_MIME, String.class, FILE_NAME))
                .metadataExtractorRegistry(registry -> registry.metadataToExtract(FILE_EXT_MIME, String.class, FILE_EXT))
                .encoders(encoders -> encoders.add(new SimpleAuthenticationEncoder()));

    }

    @Bean
    @Primary
    public RSocketMessageHandler messageHandler(RSocketStrategies rSocketStrategies,
                                                ObjectProvider<RSocketMessageHandlerCustomizer> customizers) {
        RSocketMessageHandler messageHandler = new MyRSocketMessageHandler();
        messageHandler.setRSocketStrategies(rSocketStrategies);
        customizers.orderedStream().forEach((customizer) -> customizer.customize(messageHandler));
        messageHandler.getArgumentResolverConfigurer().addCustomResolver(new AuthenticationPrincipalArgumentResolver());

        return messageHandler;
    }

    @Bean
    public RSocketServerCustomizer serverCustomizer() {
        return rSocketServer -> {
            rSocketServer.resume(new Resume());
//            rSocketServer.lease(leaseSpec -> leaseSpec.maxPendingRequests(256)
//                    .sender(() -> Flux.interval(Duration.ofSeconds(0), Duration.ofMinutes(2))
//                            .map(i -> Lease.create(Duration.ofMinutes(2), 10))));
        };
    }
}
