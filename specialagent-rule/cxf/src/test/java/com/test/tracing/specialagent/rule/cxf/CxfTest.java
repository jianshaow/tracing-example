package com.test.tracing.specialagent.rule.cxf;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.opentracing.contrib.specialagent.AgentRunner;
import io.opentracing.mock.MockTracer;

@RunWith(AgentRunner.class)
@AgentRunner.Config(disable = "*")
public class CxfTest {

	@BeforeClass
	public static void beforeClass(final MockTracer tracer) {
		Thread.currentThread().setContextClassLoader(CxfTest.class.getClassLoader());
	}

	@Test
	public void test(final MockTracer tracer) {
		// prepare server
		final JAXRSServerFactoryBean serverFactory = new JAXRSServerFactoryBean();
		serverFactory.setAddress("http://127.0.0.1:48080");
		serverFactory.setServiceBean(new EchoImpl());
		serverFactory.create();

		// prepare client
		final JAXRSClientFactoryBean clientFactory = new JAXRSClientFactoryBean();
		clientFactory.setServiceClass(Echo.class);
		clientFactory.setAddress("http://127.0.0.1:48080");
		final Echo echo = clientFactory.create(Echo.class);

		final String response = echo.echo("hello");
		assertEquals("hello", response);
		assertEquals(2, tracer.finishedSpans().size());
	}

	@Path("/")
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
