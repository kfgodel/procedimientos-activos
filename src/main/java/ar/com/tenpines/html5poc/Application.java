package ar.com.tenpines.html5poc;

import ar.com.kfgodel.webbyconvention.DefaultConfiguration;
import ar.com.kfgodel.webbyconvention.WebServer;
import ar.com.kfgodel.webbyconvention.WebServerConfiguration;
import ar.com.tenpines.orm.api.DbCoordinates;
import ar.com.tenpines.orm.api.HibernateOrm;
import ar.com.tenpines.orm.impl.HibernateFacade;
import ar.com.tenpines.orm.impl.config.ImmutableCoordinates;
import ar.com.tenpines.orm.impl.config.SmallAppPreConfig;
import org.hibernate.dialect.HSQLDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This type represents the whole application as a single object.<br>
 *     From this instance you can access the application components
 *     
 * Created by kfgodel on 22/03/15.
 */
public class Application {
    public static Logger LOG = LoggerFactory.getLogger(Application.class);

    private WebServer webServer;
    private HibernateOrm hibernate;

    public WebServer getWebServer() {
        return webServer;
    }

    public HibernateOrm getHibernate() {
        return hibernate;
    }

    public static Application create() {
        Application application = new Application();
        return application;
    }

    /**
     * Initializes application components and starts the server to listen for requests
     */
    public void start() {
        LOG.info("Starting APP");
        this.initialize();
        this.webServer.startAndJoin();
    }

    public void stop(){
        LOG.info("Stopping APP");
        this.webServer.stop();
        this.hibernate.close();
    }

    private void initialize() {
        this.hibernate = createPersistenceLayer();
        this.webServer = createWebServer();
        registerCleanupHook();
    }

    private void registerCleanupHook() {
        Runtime.getRuntime().addShutdownHook( new Thread(()->{
            LOG.info("Cleaning-up before shutdown");
            this.stop();
        }, "cleanup-thread" ));
    }

    private HibernateOrm createPersistenceLayer() {
        DbCoordinates dbCoordinates = ImmutableCoordinates.create(HSQLDialect.class, "jdbc:hsqldb:mem:testdb", "sa", "");
        HibernateOrm hibernateOrm = HibernateFacade.create(SmallAppPreConfig.create(dbCoordinates, "ar.com.tenpines.html5poc.persistent"));
        return hibernateOrm;
    }

    private WebServer createWebServer() {
        WebServerConfiguration serverConfig = DefaultConfiguration.create()
                .listeningHttpOn(9090)
                .withInjections((binder)->{
                    binder.bind(this).to(Application.class);
                });
        return WebServer.createFor(serverConfig);
    }
}
