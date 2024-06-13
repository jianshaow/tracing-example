package com.test.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serial;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

public class AuralServiceServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = -7766401686496991505L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (final InputStream in = req.getInputStream(); final PrintWriter out = resp.getWriter()) {
            resp.setContentType("text/plain");
            final String msg = IOUtils.toString(in, StandardCharsets.UTF_8);
            out.print(msg);
            in.close();
            out.close();
        }
    }
}
