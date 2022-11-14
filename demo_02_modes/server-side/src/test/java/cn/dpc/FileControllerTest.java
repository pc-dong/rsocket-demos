package cn.dpc;

import io.rsocket.metadata.WellKnownMimeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.rsocket.context.LocalRSocketServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.rsocket.metadata.UsernamePasswordMetadata;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;

import java.util.UUID;

import static cn.dpc.config.Constants.FILE_EXT_MIME;
import static cn.dpc.config.Constants.FILE_NAME_MIME;

@SpringBootTest(properties = "spring.rsocket.server.port=0")
public class FileControllerTest {

    @LocalRSocketServerPort
    int port;

    @Autowired
    RSocketRequester.Builder builder;

    @Autowired
    RSocketMessageHandler messageHandler;

    @Value("classpath:/images/image.png")
    private Resource resource;

    RSocketRequester requester;

    @BeforeEach
    public void init() {
        requester = builder.rsocketStrategies(messageHandler.getRSocketStrategies())
                .tcp("127.0.0.1", port);
    }

    @Test
    public void should_upload_success() {
        String fileName = "image_" + UUID.randomUUID().toString();
        String fileExt = "png";

        UsernamePasswordMetadata credentials = new UsernamePasswordMetadata("jdoe", "rsocket");

        Flux<DataBuffer> resourceFlux = DataBufferUtils.read(this.resource, new DefaultDataBufferFactory(), 1024)
                .doOnNext(s -> System.out.println("文件上传：" + s));

        requester.route("file.upload")
                .metadata(metadataSpec -> {
                            System.out.println("[上传测试]文件名： " + fileName + "." + fileExt);
                            metadataSpec.metadata(fileName, FILE_NAME_MIME);
                            metadataSpec.metadata(fileExt, FILE_EXT_MIME);
                            metadataSpec.metadata(credentials, MimeType.valueOf(WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION.getString()));
                        }
                ).data(resourceFlux)
                .retrieveFlux(UploadStatus.class)
                .doOnNext(o -> System.out.println("[上传文件进度]：" + o))
                .blockLast();
    }
}
