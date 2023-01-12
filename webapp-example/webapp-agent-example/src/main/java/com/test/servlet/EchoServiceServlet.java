package com.test.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EchoServiceServlet extends HttpServlet {

    private static Logger logger = LoggerFactory.getLogger(EchoServiceServlet.class);

    private static final long serialVersionUID = -7766401686496991505L;

    private HttpClient httpClient = HttpClientBuilder.create().build();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (final InputStream in = req.getInputStream(); final PrintWriter out = resp.getWriter()) {
            resp.setContentType("text/plain");
            final String msg = IOUtils.toString(in, "UTF-8");
            logger.info("be requested to echo a message: {}", msg);
            String result = this.callAuralService(msg);
            result = this.callMindService(result);
            result = this.callSpeakService(result);
            out.print(result);
            logger.info("echo back the result: {}", result);
            in.close();
            out.close();
        }
    }

    private String callAuralService(String msg) throws ClientProtocolException, IOException {
        return call(msg, "http://localhost:8080/aural");
    }

    private String callMindService(String msg) throws ClientProtocolException, IOException {
        return call(msg, "http://localhost:8080/mind");
    }
    
    private String callSpeakService(String msg) throws ClientProtocolException, IOException {
    	return call(msg, "http://localhost:8080/speak");
    }

    private String call(String msg, final String uri)
            throws UnsupportedEncodingException, IOException, ClientProtocolException {
        final HttpPost request = new HttpPost(uri);
        request.setEntity(new StringEntity(msg));
        final HttpResponse response = httpClient.execute(request);
        String result = null;
        try (InputStream in = response.getEntity().getContent()) {
            result = IOUtils.toString(in, "UTF-8");
            in.close();
        }
        return result;
    }
}
