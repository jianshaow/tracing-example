package com.test.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class SpeakServiceServlet extends HttpServlet {

	private static final long serialVersionUID = -7766401686496991505L;

	private Session session;
	
	@Override
	public void init() throws ServletException {
		session = Cluster.builder().addContactPoint("127.0.0.1").build().connect("mydb");
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
			final String msg = IOUtils.toString(in, "UTF-8");
			out.println(msg);
			in.close();
			out.close();
		}
	}

	private void accessDB() {
		session.execute("select name from application where id = ?", 1);
	}
}
