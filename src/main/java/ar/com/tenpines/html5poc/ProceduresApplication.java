package ar.com.tenpines.html5poc;

import ar.com.kfgodel.webbyconvention.WebServer;
import ar.com.kfgodel.webbyconvention.WebServerConfiguration;
import ar.com.kfgodel.webbyconvention.config.DefaultConfiguration;
import ar.com.tenpines.html5poc.components.DatabaseAuthenticator;
import ar.com.tenpines.html5poc.components.transformer.B2BTransformer;
import ar.com.tenpines.html5poc.components.transformer.TypeTransformer;
import ar.com.tenpines.orm.api.DbCoordinates;
import ar.com.tenpines.orm.api.HibernateOrm;
import ar.com.tenpines.orm.api.Preconfig;
import ar.com.tenpines.orm.impl.HibernateFacade;
import ar.com.tenpines.orm.impl.config.ImmutableDbCoordinates;
import ar.com.tenpines.orm.impl.config.SmallAppPreConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This type represents the whole application as a single object.<br>
 * From this instance you can access the application components
 * <p/>
 * Created by kfgodel on 22/03/15.
 */
public class ProceduresApplication implements Application {
  public static Logger LOG = LoggerFactory.getLogger(ProceduresApplication.class);

  private WebServer webServer;
  private HibernateOrm hibernate;
  private TypeTransformer transformer;

  @Override
  public WebServer getWebServerModule() {
    return webServer;
  }

  @Override
  public HibernateOrm getOrmModule() {
    return hibernate;
  }

  @Override
  public TypeTransformer getTransformerModule() {
    return transformer;
  }

  public static Application create() {
    ProceduresApplication application = new ProceduresApplication();
    return application;
  }

  /**
   * Initializes application components and starts the server to listen for requests
   */
  @Override
  public void start() {
    LOG.info("Starting APP");
    this.initialize();
    this.webServer.startAndJoin();
  }

  @Override
  public void stop() {
    LOG.info("Stopping APP");
    this.webServer.stop();
    this.hibernate.close();
  }

  private void initialize() {
    this.hibernate = createPersistenceLayer();
    this.webServer = createWebServer(this.hibernate);
    this.transformer = createTransformer();
    registerCleanupHook();
  }

  private B2BTransformer createTransformer() {
    return B2BTransformer.create(this);
  }

  private void registerCleanupHook() {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      LOG.info("Cleaning-up before shutdown");
      this.stop();
    }, "cleanup-thread"));
  }

  private HibernateOrm createPersistenceLayer() {
    DbCoordinates dbCoordinates = ImmutableDbCoordinates.createDeductingDialect("jdbc:h2:file:./db/h2", "sa", "");
    Preconfig hibernateConfig = SmallAppPreConfig.create(dbCoordinates, "convention.persistent");
    HibernateOrm hibernateOrm = HibernateFacade.create(hibernateConfig);
    return hibernateOrm;
  }

  private WebServer createWebServer(HibernateOrm hibernateOrm) {
    WebServerConfiguration serverConfig = DefaultConfiguration.create()
      .listeningHttpOn(9090)
      .withInjections((binder) -> {
        binder.bind(this).to(Application.class);
      })
      .authenticatingWith(DatabaseAuthenticator.create(hibernateOrm));
    return WebServer.createFor(serverConfig);
  }

}
