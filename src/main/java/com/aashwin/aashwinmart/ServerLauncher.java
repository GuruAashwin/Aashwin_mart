package com.aashwin.aashwinmart;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Standard Tomcat Server Launcher for AASHWIN MART.
 */
public class ServerLauncher {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerLauncher.class);
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws Exception {
        int port = DEFAULT_PORT;
        String portStr = System.getenv("PORT");
        if (portStr != null && !portStr.trim().isEmpty()) {
            try {
                port = Integer.parseInt(portStr.trim());
            } catch (NumberFormatException ignored) {
                // fallback to default port
            }
        }

        String webappDirLocation = "src/main/webapp";
        File webappDir = new File(webappDirLocation);
        if (!webappDir.exists()) {
            webappDirLocation = "src/main/webapp";
        }

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.setBaseDir("target/tomcat-temp");
        tomcat.getHost().setAppBase("target");
        tomcat.getHost().setAutoDeploy(false);
        tomcat.getConnector(); // initializes default HTTP connector

        // Set parent classloader
        ClassLoader classLoader = ServerLauncher.class.getClassLoader();
        tomcat.getEngine().setParentClassLoader(classLoader);

        StandardContext ctx = (StandardContext) tomcat.addWebapp("", new File(webappDirLocation).getAbsolutePath());
        ctx.setParentClassLoader(classLoader);

        // Configure JarScanner to avoid scanning unrelated system jars and enable class folder scanning
        StandardJarScanner jarScanner = new StandardJarScanner();
        jarScanner.setScanManifest(false);
        ctx.setJarScanner(jarScanner);

        // Add target/classes to webapp classloader
        File additionWebInfClasses = new File("target/classes");
        if (additionWebInfClasses.exists()) {
            WebResourceRoot resources = new StandardRoot(ctx);
            resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                    additionWebInfClasses.getAbsolutePath(), "/"));
            ctx.setResources(resources);
        }

        LOGGER.info("Starting AASHWIN MART Tomcat Server on port {}...", port);
        tomcat.start();
        LOGGER.info("===================================================================");
        LOGGER.info("AASHWIN MART Server successfully running at: http://localhost:{}/", port);
        LOGGER.info("===================================================================");
        
        tomcat.getServer().await();
    }
}
