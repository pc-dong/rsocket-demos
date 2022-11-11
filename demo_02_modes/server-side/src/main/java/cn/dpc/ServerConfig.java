package cn.dpc;

import io.rsocket.core.Resume;
import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer;
import org.springframework.boot.rsocket.server.RSocketServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cn.dpc.Constants.CLIENT_ID;
import static cn.dpc.Constants.CLIENT_ID_MIME;

@Configuration
public class ServerConfig {

    @Bean
    public RSocketStrategiesCustomizer strategiesCustomizer() {
        return strategies -> strategies
                .metadataExtractorRegistry(registry -> registry.metadataToExtract(CLIENT_ID_MIME, String.class, CLIENT_ID));
    }

    @Bean
    public RSocketServerCustomizer serverCustomizer() {
        return rSocketServer -> rSocketServer.resume(new Resume());
    }
}
