package cn.dpc;


import org.apache.jmeter.samplers.SampleResult;
import org.junit.jupiter.api.Test;

class RSocketTestTest {

    @Test
    void runTest() {
        RSocketTest rSocketTest = new RSocketTest();

        SampleResult sr =  rSocketTest.runTest(null);
        System.out.println(sr.isSuccessful());
        System.out.println(sr.getResponseDataAsString());
    }
}