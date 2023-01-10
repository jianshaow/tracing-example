package com.test.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

public class MindServiceServlet extends HttpServlet {

    private static Logger logger = LoggerFactory.getLogger(MindServiceServlet.class);

    private static final long serialVersionUID = -7766401686496991505L;

    private Jedis jedis = new Jedis();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        accessDB();
        try (final InputStream in = req.getInputStream(); final PrintWriter out = resp.getWriter()) {
            resp.setContentType("text/plain");
            final String msg = IOUtils.toString(in, "UTF-8");
            out.print(msg);
            in.close();
            out.close();
        }
    }

    private void accessDB() {
        final Integer appId = 1;
        String appName = jedis.get(appId.toString());
        if (appName == null) {
            DataSource ds = null;
            try {
                final InitialContext ctx = new InitialContext();
                ds = (DataSource) ctx.lookup("jdbc/test.ds");
                try (final Connection conn = ds.getConnection();
                        final PreparedStatement stmt = conn
                                .prepareStatement("select name from application where id = ?")) {
                    stmt.setInt(1, appId);
                    final ResultSet result = stmt.executeQuery();
                    if (result.next()) {
                        appName = result.getString(1);
                    }
                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage(), e);
                }
            } catch (NamingException e) {
                logger.error(e.getMessage(), e);
            }
            if (appName != null) {
                jedis.setex(appId.toString(), 10, appName);
            }
        }
    }
}
