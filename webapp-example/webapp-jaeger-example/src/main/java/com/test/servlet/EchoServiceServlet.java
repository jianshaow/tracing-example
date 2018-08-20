package com.test.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoServiceServlet extends HttpServlet {

    private static Logger logger = LoggerFactory.getLogger(EchoServiceServlet.class);

    private static final long serialVersionUID = -7766401686496991505L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        accessDB();
        resp.setContentType("text/plain");
        try (final InputStream in = req.getInputStream(); final OutputStream out = resp.getOutputStream()) {
            byte[] buffer = new byte[100];
            while (in.read(buffer) != -1) {
                out.write(buffer);
            }
            out.close();
            in.close();
        }
    }

    private void accessDB() {
        DataSource ds = null;
        try {
            final InitialContext ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("jdbc/test.ds");
            try (final Connection conn = ds.getConnection(); final Statement stmt = conn.createStatement()) {
                stmt.executeQuery("select * from application");
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        } catch (NamingException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
