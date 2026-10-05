package com.aashwin.aashwinmart;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

/**
 * Embedded Tomcat launcher for local development.
 * Deploys target/aashwinmart.war into an embedded Tomcat 9 instance.
 *
 * Run with: mvn package -DskipTests exec:java
 * Access at: http://localhost:8080/aashwinmart/
 */
public class EmbeddedServer {

    private static final int DEFAULT_PORT = 8080;
    private static final String CONTEXT_PATH = "/aashwinmart";

    public static void main(String[] args) throws LifecycleException {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {
                // use default
            }
        }

        // Resolve WAR file path
        File war = new File("target/aashwinmart.war");
        if (!war.exists()) {
            System.err.println("ERROR: WAR not found at " + war.getAbsolutePath());
            System.err.println("Run 'mvn package -DskipTests' first, then 'mvn exec:java'");
            System.exit(1);
        }

        // Set Tomcat base directory to a temp location
        File baseDir = new File(System.getProperty("java.io.tmpdir"), "aashwinmart-tomcat");
        baseDir.mkdirs();

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.setBaseDir(baseDir.getAbsolutePath());
        tomcat.getConnector();

        // Deploy WAR — Tomcat will unpack it into baseDir/webapps
        Context ctx = tomcat.addWebapp(CONTEXT_PATH, war.getAbsolutePath());

        // Prevent Tomcat from scanning JARs for TLD/annotation — speeds up startup
        ctx.setJarScanner(new org.apache.tomcat.util.scan.StandardJarScanner());

        System.out.println("=================================================");
        System.out.println(" AASHWIN MART — Embedded Tomcat Server");
        System.out.println(" WAR     : " + war.getAbsolutePath());
        System.out.println(" Port    : " + port);
        System.out.println(" Context : " + CONTEXT_PATH);
        System.out.println(" URL     : http://localhost:" + port + CONTEXT_PATH + "/");
        System.out.println("=================================================");

        tomcat.start();
        System.out.println(" Server started successfully! Press Ctrl+C to stop.");
        tomcat.getServer().await();
    }
}
