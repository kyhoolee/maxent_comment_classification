package id.co.babe.filter.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;


import id.co.babe.filter.CommentFilter;
import id.co.babe.util.ConfigParams;

public class HttpApp {
	


    private static class filter extends HttpServlet {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            
            PrintWriter out = resp.getWriter();
            try {
            	String comment = req.getParameter("comment");
            	if(comment != null && comment.length() > 0) {
            		String res = CommentFilter.ruleInference(comment);
            		out.print(res);
            	} else {
            		out.print("-1");
            	}
            	
                out.flush();
                out.close();
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }
    }
	
    public static void main(String[] args) throws Exception {
    	ConfigParams.load();
        Server server = new Server(Integer.valueOf(ConfigParams.http_port));
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new filter()), "/filter");
        server.start();
        server.join();
    }

}
