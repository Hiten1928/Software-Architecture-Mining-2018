package com.sparkSAM2018;

//import java.io.InputStream;
import java.io.InputStream;
import java.util.Objects;
// java.util.logging.LogManager;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.sparkSAM2018.application.SAMCenter;
import spark.TemplateEngine;
import spark.template.freemarker.FreeMarkerEngine;
import com.sparkSAM2018.ui.WebServer;


/**
 * The entry point for the WebCheckers web application.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public final class Application {
    private static final Logger LOG = Logger.getLogger(Application.class.getName());

    //
    // Application Launch method
    //

    /**
     * Entry point for the SAM 2018 web application.
     *
     * <p>
     * It wires the application components together.  This is an example
     * of <a href='https://en.wikipedia.org/wiki/Dependency_injection'>Dependency Injection</a>
     * </p>
     *
     * @param args
     *    Command line arguments; none expected.
     */
    public static void main(String[] args) {
        // initialize Logging
        /*try {
            ClassLoader classLoader = Application.class.getClassLoader();
            final InputStream logConfig = classLoader.getResourceAsStream("log.properties");
            LogManager.getLogManager().readConfiguration(logConfig);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Could not initialize log manager because: " + e.getMessage());
        }*/

        // create the one and only checkers center

        final SAMCenter checkersCenter = new SAMCenter();

        // The application uses FreeMarker templates to generate the HTML
        // responses sent back to the client. This will be the engine processing
        // the templates and associated data.
        final TemplateEngine templateEngine = new FreeMarkerEngine();

        // inject the game center and freemarker engine into web server
        final WebServer webServer = new WebServer(checkersCenter,templateEngine);

        // inject web server into application
        final Application app = new Application(webServer);

        // start the application up
        app.initialize();
    }

    //
    // Attributes
    //

    private final WebServer webServer;

    //
    // Constructor
    //

    private Application(final WebServer webServer) {
        // validation
        Objects.requireNonNull(webServer, "webServer must not be null");
        //
        this.webServer = webServer;
    }

    //
    // Private methods
    //

    private void initialize() {
        LOG.fine("SAM 2018 is initializing.");

        // configure Spark and startup the Jetty web server
        webServer.initialize();

        // other applications might have additional services to configure

        LOG.fine("SAM 2018 initialization complete.");
    }

}