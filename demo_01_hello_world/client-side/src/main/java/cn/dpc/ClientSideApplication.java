package cn.dpc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.rsocket.RSocketRequester;

@SpringBootApplication
public class ClientSideApplication implements CommandLineRunner {
    @Autowired
    RSocketRequester requester;

    public static void main(String[] args) {
        SpringApplication.run(ClientSideApplication.class, args);
    }

    @Override
    public void run(String... args) {
        requester.rsocketClient().connect();
    }
}
