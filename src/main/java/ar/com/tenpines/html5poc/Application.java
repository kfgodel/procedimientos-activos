package ar.com.tenpines.html5poc;

import ar.com.kfgodel.webbyconvention.DefaultConfiguration;
import ar.com.kfgodel.webbyconvention.WebServer;
import ar.com.kfgodel.webbyconvention.WebServerConfiguration;

/**
 * This type represents the whole application as a single object.<br>
 *     From this instance you can access the application components
 *     
 * Created by kfgodel on 22/03/15.
 */
public class Application {

    private WebServer webServer;

    public static Application create() {
        Application application = new Application();
        return application;
    }

    /**
     * Initializes application components and starts the server to listen for requests
     */
    public void start() {
        this.initialize();
        this.webServer.startAndJoin();
    }

    private void initialize() {
        WebServerConfiguration serverConfig = DefaultConfiguration.create()
                .listeningHttpOn(9090)
                .withInjections((binder)->{
                    binder.bind(this).to(Application.class);
                });
        this.webServer = WebServer.createFor(serverConfig);
    }
}
