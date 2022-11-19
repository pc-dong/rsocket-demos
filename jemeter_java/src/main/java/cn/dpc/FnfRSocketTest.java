package cn.dpc;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;
import org.springframework.http.codec.cbor.Jackson2CborEncoder;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;

import java.util.concurrent.CountDownLatch;

public class FnfRSocketTest extends AbstractJavaSamplerClient {
    private static RSocketRequester rsocketRequester;

    @Override
    public void setupTest(JavaSamplerContext context) {
        super.setupTest(context);
        String host = context.getParameter("host");
        int port = context.getIntParameter("port");
        rsocketRequester = RSocketRequester.builder()
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
                .tcp(host, port);
    }

    @Override
    public Arguments getDefaultParameters() {
        Arguments arguments = new Arguments();
        arguments.addArgument("host", "127.0.0.1");
        arguments.addArgument("port", "7001");
        return arguments;
    }

    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult sr = new SampleResult();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            sr.sampleStart();
            rsocketRequester.route("log")
                    .data("hello")
                    .retrieveMono(Void.class)
                    .doOnError(error -> {
                                sr.setSuccessful(false);
                                error.printStackTrace();
                            }
                    ).doOnSuccess(item -> {
                        sr.setResponseData(String.valueOf(item), null);
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
