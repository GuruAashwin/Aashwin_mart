package com.aashwin.aashwinmart.controller.api;

import com.aashwin.aashwinmart.config.DatabaseConfig;
import com.aashwin.aashwinmart.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Health check endpoint verifying actual application and database connectivity.
 * URL: GET /api/v1/health
 */
@WebServlet(name = "ApiHealthServlet", urlPatterns = {"/api/v1/health"})
public class ApiHealthServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiHealthServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");

        boolean dbUp = false;
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT 1")) {
            if (rs.next() && rs.getInt(1) == 1) {
                dbUp = true;
            }
        } catch (Exception e) {
            LOGGER.error("Health check failed on database connection", e);
        }

        health.put("db", dbUp ? "UP" : "DOWN");

        int statusCode = dbUp ? HttpServletResponse.SC_OK : HttpServletResponse.SC_SERVICE_UNAVAILABLE;
        JsonUtil.writeJsonResponse(response, statusCode, health);
    }
}
