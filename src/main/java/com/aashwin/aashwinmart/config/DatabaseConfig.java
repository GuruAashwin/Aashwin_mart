package com.aashwin.aashwinmart.config;

import com.aashwin.aashwinmart.util.DbUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Singleton configuration and manager for HikariCP Connection Pool and H2 Database.
 */
public final class DatabaseConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConfig.class);
    private static HikariDataSource dataSource;

    private DatabaseConfig() {
    }

    /**
     * Initializes the HikariCP connection pool using application properties.
     */
    public static synchronized void initialize() {
        if (dataSource != null && !dataSource.isClosed()) {
            LOGGER.info("HikariCP DataSource is already initialized");
            return;
        }

        String driver = AppConfig.get("db.driver", "org.h2.Driver");
        String url = AppConfig.get("db.url", "jdbc:h2:./data/aashwinmart;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE;MODE=LEGACY");
        String user = AppConfig.get("db.user", "sa");
        String password = AppConfig.get("db.password", "");

        int minIdle = AppConfig.getInt("db.pool.minIdle", 2);
        int maxSize = AppConfig.getInt("db.pool.maxSize", 10);
        long idleTimeout = AppConfig.getInt("db.pool.idleTimeout", 30000);
        long maxLifetime = AppConfig.getInt("db.pool.maxLifetime", 1800000);
        long connectionTimeout = AppConfig.getInt("db.pool.connectionTimeout", 30000);

        LOGGER.info("Initializing HikariCP DataSource: URL={}, PoolSize={}-{}", url, minIdle, maxSize);

        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driver);
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setMinimumIdle(minIdle);
        config.setMaximumPoolSize(maxSize);
        config.setIdleTimeout(idleTimeout);
        config.setMaxLifetime(maxLifetime);
        config.setConnectionTimeout(connectionTimeout);
        config.setPoolName("AashwinMartHikariPool");

        // H2 optimizations
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);
        LOGGER.info("HikariCP DataSource initialized successfully");

        // Run automated migrations and seed check
        try (Connection conn = dataSource.getConnection()) {
            DbUtil.runMigrations(conn);
        } catch (Exception e) {
            LOGGER.error("Failed to execute database migrations on startup", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    /**
     * Initializes an in-memory DataSource specifically for unit/integration tests.
     *
     * @param jdbcUrl Test JDBC URL
     */
    public static synchronized void initializeForTest(String jdbcUrl) {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }

        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.h2.Driver");
        config.setJdbcUrl(jdbcUrl != null ? jdbcUrl : "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=LEGACY");
        config.setUsername("sa");
        config.setPassword("");
        config.setMinimumIdle(1);
        config.setMaximumPoolSize(5);
        config.setPoolName("AashwinMartTestPool");

        dataSource = new HikariDataSource(config);
        try (Connection conn = dataSource.getConnection()) {
            DbUtil.runMigrations(conn);
        } catch (Exception e) {
            LOGGER.error("Failed to initialize test database", e);
            throw new RuntimeException("Test database initialization failed", e);
        }
    }

    /**
     * Returns a pooled database connection.
     *
     * @return Connection from HikariCP pool
     * @throws SQLException If pool connection cannot be acquired
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null || dataSource.isClosed()) {
            initialize();
        }
        return dataSource.getConnection();
    }

    public static DataSource getDataSource() {
        if (dataSource == null || dataSource.isClosed()) {
            initialize();
        }
        return dataSource;
    }

    /**
     * Closes the HikariCP DataSource on servlet context shutdown.
     */
    public static synchronized void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            LOGGER.info("Closing HikariCP DataSource pool");
            dataSource.close();
            dataSource = null;
        }
    }
}
