package com.test.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serial;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class SpeakServiceServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = -7766401686496991505L;

    private Session session;

    @Override
    public void init() throws ServletException {
        session = Cluster.builder().addContactPoint("host.docker.internal").build().connect("mydb");
    }

    @Override
    public void destroy() {
        final Cluster cluster = session.getCluster();
        session.close();
        cluster.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        accessDB();
        try (final InputStream in = req.getInputStream(); final PrintWriter out = resp.getWriter()) {
            resp.setContentType("text/plain");
            final String msg = IOUtils.toString(in, StandardCharsets.UTF_8);
            out.print(msg);
            in.close();
            out.close();
        }
    }

    private void accessDB() {
        session.execute("select name from application where id = ?", 1);
    }
}
