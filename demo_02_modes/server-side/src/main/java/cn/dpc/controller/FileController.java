package cn.dpc.controller;

import cn.dpc.UploadStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;

import static cn.dpc.config.Constants.FILE_EXT;
import static cn.dpc.config.Constants.FILE_NAME;

@Controller
@Slf4j
public class FileController {

    @Value("${output.file.path:upload}")
    private Path outputPath;

    @MessageMapping("file.upload")
    public Flux<UploadStatus> upload(@Headers Map<String, Object> metadata,
                                     @Payload Flux<DataBuffer> content
    ) throws Exception {
        log.info("[上传传路径]：{}", outputPath);

        var fileName = metadata.get(FILE_NAME);
        var fileExt = metadata.get(FILE_EXT);
        var path = Paths.get(fileName + "." + fileExt);

        log.info("[文件上传] fileName={}, fileExt={}, path={}", fileName, fileExt, path);

        AsynchronousFileChannel channel = AsynchronousFileChannel.open(this.outputPath.resolve(path),
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE);

        return Flux.concat(
                DataBufferUtils.write(content, channel)
                .map(s -> UploadStatus.CHUNK_COMPLETED), Mono.just(UploadStatus.COMPLETED))
                .doOnComplete(() -> {
                    try {
                        channel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).onErrorReturn(UploadStatus.FAILED);
    }
}
