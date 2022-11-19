package cn.dpc;

import io.rsocket.util.DefaultPayload;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;
import org.springframework.http.codec.cbor.Jackson2CborEncoder;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.MimeType;

import java.util.concurrent.CountDownLatch;

public class RSocketTest extends AbstractJavaSamplerClient {
    private static final RSocketRequester rsocketRequester = RSocketRequester.builder()
            .rsocketStrategies(RSocketStrategies.builder()
                    .encoders(encoders -> {
                        encoders.add(new Jackson2CborEncoder());
                        encoders.add(new Jackson2JsonEncoder());
                    })
                    .decoders(decoders -> {
                        decoders.add(new Jackson2JsonDecoder());
                        decoders.add(new Jackson2CborDecoder());
                    })
                    .build())
            .tcp("127.0.0.1", 7001);

    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult sr = new SampleResult();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            sr.sampleStart();
            rsocketRequester.route("toUpperCase")
                    .metadata(metadataSpec -> metadataSpec.metadata("111", MimeType.valueOf("message/x.client.id")))
                    .data(DefaultPayload.create("hello"))
                    .retrieveMono(String.class)
                    .doOnError(error -> {
                                sr.setSuccessful(false);
                                error.printStackTrace();
                            }
                    ).doOnSuccess(item -> {
                        sr.setResponseData(item, null);
                        sr.setSuccessful(true);
                    }).doOnTerminate(() -> countDownLatch.countDown())
                    .subscribe();
            countDownLatch.await();
        } catch (Exception e) {
            sr.setSuccessful(false);
            e.printStackTrace();
        } finally {
            sr.sampleEnd();
        }

        return sr;
    }
}
