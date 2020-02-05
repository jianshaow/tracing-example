package com.test.tracing.specialagent.rule.filter;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.opentracing.contrib.specialagent.AgentRunner;
import io.opentracing.mock.MockSpan;
import io.opentracing.mock.MockTracer;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RunWith(AgentRunner.class)
@AgentRunner.Config(disable = "*")
public class FilterTest {

	// jetty starts on random port
	private static int serverPort;
	private static Server server;

	@BeforeClass	
	public static void beforeClass(final MockTracer tracer) throws Exception {
		server = new Server(0);
		
		final ServletHandler servletHandler = new ServletHandler();
		servletHandler.addServletWithMapping(MockServlet.class, "/hello");
		server.setHandler(servletHandler);
		
		server.start();
		serverPort = ((ServerConnector) server.getConnectors()[0]).getLocalPort();
	}

	@Test
	public void testHelloRequest(final MockTracer tracer) throws Exception {

		MockServlet.count = 0;

		final OkHttpClient client = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).build();
		final Request request = new Request.Builder().url("http://localhost:" + serverPort + "/hello").build();
		final Response response = client.newCall(request).execute();

		assertEquals("MockServlet response", HttpServletResponse.SC_ACCEPTED, response.code());
		assertEquals("MockServlet count", 1, MockServlet.count);

		final List<MockSpan> spans = tracer.finishedSpans();
		assertEquals("MockTracer spans: " + spans, 1, spans.size());
	}

	@AfterClass
	public static void afterClass() throws Exception {
		server.stop();
		server.join();
	}

	public static class MockServlet extends HttpServlet {
		private static final long serialVersionUID = 976450353590523027L;

		static volatile int count;

		@Override
		public void doGet(final HttpServletRequest request, final HttpServletResponse response)
				throws IOException, ServletException {
			++count;
			response.setStatus(HttpServletResponse.SC_ACCEPTED);
		}
	}
}
