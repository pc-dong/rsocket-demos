package cn.dpc;


import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.jupiter.api.Test;

class RSocketTestTest {

    @Test
    void runTest() {
        RSocketTest rSocketTest = new RSocketTest();

        JavaSamplerContext context = new JavaSamplerContext(rSocketTest.getDefaultParameters());
        rSocketTest.setupTest(context);

        SampleResult sr =  rSocketTest.runTest(null);
        System.out.println(sr.isSuccessful());
        System.out.println(sr.getResponseDataAsString());
    }
}