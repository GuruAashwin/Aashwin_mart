package com.aashwin.aashwinmart.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database utility for migration execution, seeding, and transaction management.
 */
public final class DbUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbUtil.class);

    private static final String[] MIGRATION_FILES = {
            "db/migrations/V1__init_schema.sql",
            "db/migrations/V2__add_reviews_table.sql",
            "db/migrations/V3__add_index_orders_status.sql",
            "db/migrations/V4__add_user_is_active.sql"
    };

    private DbUtil() {
    }

    /**
     * Executes numbered migrations in sequential order on application startup.
     *
     * @param conn Open database connection
     */
    public static void runMigrations(Connection conn) {
        LOGGER.info("Starting database migration execution...");
        for (String migrationFile : MIGRATION_FILES) {
            LOGGER.info("Executing migration: {}", migrationFile);
            executeSqlScript(conn, migrationFile);
        }
        LOGGER.info("Database migrations completed successfully.");

        // Check if database needs initial seeding
        if (isTableEmpty(conn, "users")) {
            LOGGER.info("Users table is empty. Running initial seed data...");
            executeSqlScript(conn, "seed.sql");
            LOGGER.info("Initial seed data loaded successfully.");
        }
    }

    /**
     * Executes an SQL script resource file line-by-line or statement-by-statement.
     *
     * @param conn Database connection
     * @param resourcePath Classpath resource path
     */
    public static void executeSqlScript(Connection conn, String resourcePath) {
        try (InputStream in = DbUtil.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                // Try leading slash
                try (InputStream inSlash = DbUtil.class.getClassLoader().getResourceAsStream("/" + resourcePath)) {
                    if (inSlash == null) {
                        LOGGER.warn("SQL script not found in classpath: {}", resourcePath);
                        return;
                    }
                    executeScriptStream(conn, inSlash);
                }
            } else {
                executeScriptStream(conn, in);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to execute SQL script: {}", resourcePath, e);
            throw new RuntimeException("SQL script execution failed: " + resourcePath, e);
        }
    }

    private static void executeScriptStream(Connection conn, InputStream in) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim();
                if (trimmedLine.startsWith("--") || trimmedLine.isEmpty()) {
                    continue;
                }
                // Strip inline comments if any
                int commentIndex = trimmedLine.indexOf("--");
                if (commentIndex >= 0) {
                    trimmedLine = trimmedLine.substring(0, commentIndex).trim();
                }
                sb.append(trimmedLine).append(" ");
            }
        }

        String[] statements = sb.toString().split(";");
        try (Statement stmt = conn.createStatement()) {
            for (String sql : statements) {
                String trimmed = sql.trim();
                if (!trimmed.isEmpty()) {
                    stmt.execute(trimmed);
                }
            }
        }
    }

    /**
     * Checks if a table has zero rows.
     */
    public static boolean isTableEmpty(Connection conn, String tableName) {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName)) {
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            // Table might not exist yet
            return true;
        }
        return true;
    }

    /**
     * Rolls back a transaction quietly without throwing checked exceptions.
     */
    public static void rollbackQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                LOGGER.error("Failed to rollback connection transaction", e);
            }
        }
    }

    /**
     * Closes an AutoCloseable resource quietly.
     */
    public static void closeQuietly(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ignored) {
                // Suppress
            }
        }
    }
}
