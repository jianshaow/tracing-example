package com.test.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MindServiceServlet extends HttpServlet {

    private static Logger logger = LoggerFactory.getLogger(MindServiceServlet.class);

    private static final long serialVersionUID = -7766401686496991505L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        accessDB();
        try (final InputStream in = req.getInputStream(); final PrintWriter out = resp.getWriter()) {
            resp.setContentType("text/plain");
            final String msg = IOUtils.toString(in, "UTF-8");
            out.println(msg);
            in.close();
            out.close();
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
