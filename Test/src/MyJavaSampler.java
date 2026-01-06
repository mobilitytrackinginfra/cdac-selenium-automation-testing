

import org.apache.jmeter.protocol.java.sampler.JavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.io.File;

import org.apache.jmeter.config.Arguments;

public class MyJavaSampler implements JavaSamplerClient {

    @Override
    public void setupTest(JavaSamplerContext context) {
        // Initialize resources
    }

    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult result = new SampleResult();
        result.sampleStart();

        try {
            // Business logic
        	boolean res = new File("D:\\CDAC\\JMeter\\JMeterJava.txt").createNewFile();
            Thread.sleep(100);
            result.setSuccessful(res);
            result.setResponseMessage("Success");
        } catch (Exception e) {
            result.setSuccessful(false);
            result.setResponseMessage("Failure");
        } finally {
            result.sampleEnd();
        }

        return result;
    }

    @Override
    public void teardownTest(JavaSamplerContext context) {
        // Cleanup
    }

    @Override
    public Arguments getDefaultParameters() {
        Arguments args = new Arguments();
        args.addArgument("param1", "value1");
        return args;
    }
}
