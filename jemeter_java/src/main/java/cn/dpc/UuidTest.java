package cn.dpc;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.util.UUID;

public class UuidTest extends AbstractJavaSamplerClient {
    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult sr = new SampleResult();
        try {
            sr.sampleStart();
            String id = UUID.randomUUID().toString();
            sr.setResponseData(id, null);
            sr.setDataType("text");
            sr.setSuccessful(true);
        } catch (Exception e) {
            sr.setSuccessful(false);
            e.printStackTrace();
        }finally {
            sr.sampleEnd();
        }

        return sr;
    }
}
