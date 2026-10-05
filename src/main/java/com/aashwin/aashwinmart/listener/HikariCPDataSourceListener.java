package com.aashwin.aashwinmart.listener;

import com.aashwin.aashwinmart.config.DatabaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * ServletContextListener that initializes and shuts down HikariCP connection pool.
 */
@WebListener
public class HikariCPDataSourceListener implements ServletContextListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(HikariCPDataSourceListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("===================================================================");
        LOGGER.info("Starting AASHWIN MART Servlet Context...");
        LOGGER.info("Initializing HikariCP DataSource and Running Database Migrations...");
        try {
            DatabaseConfig.initialize();
            LOGGER.info("AASHWIN MART Database Connection Pool Initialized Successfully!");
            LOGGER.info("===================================================================");
        } catch (Exception e) {
            LOGGER.error("CRITICAL ERROR: Failed to initialize database on startup", e);
            throw new RuntimeException("Application startup aborted due to DB error", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("===================================================================");
        LOGGER.info("Shutting down AASHWIN MART Servlet Context...");
        DatabaseConfig.shutdown();
        LOGGER.info("HikariCP DataSource Pool Closed Successfully.");
        LOGGER.info("===================================================================");
    }
}
