package cn.dpc;

import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StreamRSocketTestTest {

    @Test
    void setupTest() {
        StreamRSocketTest rSocketTest = new StreamRSocketTest();
        JavaSamplerContext context = new JavaSamplerContext(rSocketTest.getDefaultParameters());
        rSocketTest.setupTest(context);

        SampleResult sr =  rSocketTest.runTest(null);
        System.out.println(sr.isSuccessful());
        System.out.println(sr.getResponseDataAsString());
    }
}