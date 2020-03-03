package com.test.tracing.specialagent.rule.cxf;

import static org.junit.Assert.assertEquals;

import javax.jws.WebService;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.opentracing.contrib.specialagent.AgentRunner;
import io.opentracing.mock.MockTracer;

@RunWith(AgentRunner.class)
@AgentRunner.Config(disable = "*")
public class CxfTest {

	private static final String BASE_URI = "http://127.0.0.1:48080";

	@BeforeClass
	public static void beforeClass(final MockTracer tracer) {
		Thread.currentThread().setContextClassLoader(CxfTest.class.getClassLoader());
	}
	
	@Before
	public void before(final MockTracer tracer) {
		tracer.reset();
	}

	@Test
	public void testRs(final MockTracer tracer) {
		final String msg = "hello";

		// prepare server
		final JAXRSServerFactoryBean serverFactory = new JAXRSServerFactoryBean();
		serverFactory.setAddress(BASE_URI);
		serverFactory.setServiceBean(new EchoImpl());
		final Server server = serverFactory.create();

		// prepare client
		final JAXRSClientFactoryBean clientFactory = new JAXRSClientFactoryBean();
		clientFactory.setServiceClass(Echo.class);
		clientFactory.setAddress(BASE_URI);
		final Echo echo = clientFactory.create(Echo.class);

		final String response = echo.echo(msg);

		assertEquals(msg, response);
		assertEquals(2, tracer.finishedSpans().size());
		assertEquals("we tag what we want", tracer.finishedSpans().get(0).tags().get("customized.tag"));

		final WebClient client = WebClient.create(BASE_URI);
		final String result = client.post(msg, String.class);

		assertEquals(msg, result);
		assertEquals(4, tracer.finishedSpans().size());

		server.destroy();
	}

	@Test
	public void testWs(final MockTracer tracer) {
		final String msg = "hello";
		
		// prepare server
		JaxWsServerFactoryBean serverFactory = new JaxWsServerFactoryBean();
		serverFactory.setServiceClass(Echo.class);
		serverFactory.setAddress(BASE_URI);
		serverFactory.setServiceBean(new EchoImpl());
		final Server server = serverFactory.create();

		// prepare client
		final JaxWsProxyFactoryBean  clientFactory = new JaxWsProxyFactoryBean();
		clientFactory.setServiceClass(Echo.class);
		clientFactory.setAddress(BASE_URI);
		final Echo echo = (Echo) clientFactory.create();
		
		final String response = echo.echo(msg);

		assertEquals(msg, response);
		assertEquals(2, tracer.finishedSpans().size());
		assertEquals("we tag what we want", tracer.finishedSpans().get(0).tags().get("customized.tag"));

		server.destroy();
	}

	@Path("/")
	@WebService
	public static interface Echo {
		@POST
		String echo(String msg);
	}

	public static class EchoImpl implements Echo {

		@Override
		public String echo(String msg) {
			return msg;
		}
	}
}
