package be.mdi.tooling.jmetertprequest;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.nio.charset.StandardCharsets;

/*
https://dzone.com/articles/implement-custom-jmeter-samplers
*/
public class UselessJavaRequest extends AbstractJavaSamplerClient {

	// All parameters you define here are Strings
	private static final String PARAM_1 = "Parameter 1";
	private static final String PARAM_2 = "Parameter 2";
	private static final String PARAM_3 = "Parameter 3";

	// Override this to set the default values.
	// If you want empty fields, define them explicitly as
	// empty strings or zeros to avoid null values.
	@Override
	public Arguments getDefaultParameters() {
		Arguments defaultParameters = new Arguments();
		
		defaultParameters.addArgument(PARAM_1, "");
		defaultParameters.addArgument(PARAM_2, "");
		defaultParameters.addArgument(PARAM_3, "0");
		
		return defaultParameters;
	}

	/*
	 * Here we actually run the sample.
	 */
	@Override
	public SampleResult runTest(JavaSamplerContext context) {

		// Get the variables from the context passed by JMeter.
		String parameter1 = context.getParameter(PARAM_1);
		String parameter2 = context.getParameter(PARAM_2);
		// I choose to cast to the target value here.
		// Add error handling if needed.
		int parameter3 = Integer.valueOf(context.getParameter(PARAM_3));

		// Create the object
		// This can be an external object.
		// Any library handling some type of operation that you can put on the classpath
		// can be added.
		SomeUselessJavaOperation ujo = new SomeUselessJavaOperation(parameter1, parameter2, parameter3);

		// Prepare the result and start the timer.
		SampleResult sampleResult = new SampleResult();
		sampleResult.sampleStart();

		// Actual execution: in a try block to handle the request failure without blocking JMeter.
		// We want feedback. Not a crashing test application.
		try {
			// Get the response as bytes.
			// It does not need to be bytes.
			// My actual implementation on which this dudd is based required bytes to be received.
			byte[] response = ujo.doAction().getResult().getBytes(StandardCharsets.UTF_8);
			sampleResult.sampleEnd();
			
			sampleResult.setSamplerData(
					String.format(
							"{parameter_1:%s, parameter_2:%s, parameter_3:%s}",
							parameter1,
							parameter2,
							Integer.toString(parameter3)
					)
			);

			// Cast back from bytes to String
			// Of course it's better to retrieve it as String and then put it here straight.
			sampleResult.setResponseMessage(new String(response));
			sampleResult.setResponseData(response);
			sampleResult.setDataEncoding("UTF-8");
			
			if ( ujo.isSuccess() ) {
				sampleResult.setSuccessful(true);
				// Because it's not an actual HTTP request I set the response to OK when succesful.
				// This is a debatable implementation choice.
				sampleResult.setResponseCodeOK();
			} else {
				sampleResult.setSuccessful(false);
				sampleResult.setResponseData(ujo.getError().getBytes(StandardCharsets.UTF_8));
				// For any other then OK I choose 400 Bad Request.
				// This is arbitrary.
				sampleResult.setResponseCode("400");
			}
			
		} catch (Exception e) {
			sampleResult.sampleEnd();
			sampleResult.setResponseMessage(e.getMessage());
			sampleResult.setSuccessful(false);
		}

		// Then return the sampleresult to JMeter so the app can handle the rest.
		return sampleResult;
	}

}
