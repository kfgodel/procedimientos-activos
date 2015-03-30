package ar.com.tenpines.html5poc;

import ar.com.kfgodel.webbyconvention.DefaultConfiguration;
import ar.com.kfgodel.webbyconvention.WebServer;
import ar.com.kfgodel.webbyconvention.WebServerConfiguration;
import ar.com.kfgodel.webbyconvention.auth.api.WebCredential;
import ar.com.tenpines.html5poc.persistent.Usuario;
import ar.com.tenpines.orm.api.DbCoordinates;
import ar.com.tenpines.orm.api.HibernateOrm;
import ar.com.tenpines.orm.impl.HibernateFacade;
import ar.com.tenpines.orm.impl.config.ImmutableCoordinates;
import ar.com.tenpines.orm.impl.config.SmallAppPreConfig;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.dialect.HSQLDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

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
    private Usuario noUser;

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
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Cleaning-up before shutdown");
            this.stop();
        }, "cleanup-thread"));
    }

    private HibernateOrm createPersistenceLayer() {
        String closeAfterLastConnection = "shutdown=true";
        DbCoordinates dbCoordinates = ImmutableCoordinates.create(HSQLDialect.class, "jdbc:hsqldb:file:db/hsql;" + closeAfterLastConnection, "sa", "");
        HibernateOrm hibernateOrm = HibernateFacade.create(SmallAppPreConfig.create(dbCoordinates, "ar.com.tenpines.html5poc.persistent"));
        return hibernateOrm;
    }

    private WebServer createWebServer() {
        WebServerConfiguration serverConfig = DefaultConfiguration.create()
                .listeningHttpOn(9090)
                .withInjections((binder) -> {
                    binder.bind(this).to(Application.class);
                })
                .authenticatingWith(this::authenticateUsers);
        return WebServer.createFor(serverConfig);
    }

    private Optional<Object> authenticateUsers(WebCredential credentials){
        Usuario foundUser = this.getHibernate().doWithSession((context) -> {
            // If there are no users allow anyone to authenticate
            Criteria countCriteria = context.getSession().createCriteria(Usuario.class);
            countCriteria.setProjection(Projections.rowCount());
            Number userCount = (Number) countCriteria.uniqueResult();
            if (userCount.intValue() < 1) {
                return getProtoUser();
            }

            Criteria usuarioCriteria = context.getSession().createCriteria(Usuario.class);
            usuarioCriteria.add(Restrictions.eq(Usuario.login_FIELD, credentials.getUsername()));
            usuarioCriteria.add(Restrictions.eq(Usuario.password_FIELD, credentials.getPassword()));
            return (Usuario) usuarioCriteria.uniqueResult();
        });
        if(foundUser == null){
            return Optional.empty();
        }
        // Use the id as the web identification
        return Optional.of(foundUser.getId());
    }


    /**
     * Returns the user used to create other user when the application has none
     * @return The fake user
     */
    public Usuario getProtoUser(){
        if (noUser == null) {
            noUser = Usuario.create("Proto user","","");
            noUser.setId(-1L);
        }
        return noUser;
    }
}
