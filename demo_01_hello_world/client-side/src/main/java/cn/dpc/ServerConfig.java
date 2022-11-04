package cn.dpc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.util.MimeType;

@Configuration
public class ServerConfig {

    @Bean
    public RSocketRequester requester(RSocketMessageHandler handler) {
        return RSocketRequester.builder()
                .dataMimeType(new MimeType("application", "json"))
                .rsocketConnector(connector -> connector.acceptor(handler.responder()))
                .tcp("localhost", 7001);
    }
}
