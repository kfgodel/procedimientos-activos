package ar.com.kfgodel.proact;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.dependencies.impl.DependencyInjectorImpl;
import ar.com.kfgodel.proact.components.DatabaseAuthenticator;
import ar.com.kfgodel.proact.config.ProceduresConfiguration;
import ar.com.kfgodel.transformbyconvention.api.TypeTransformer;
import ar.com.kfgodel.transformbyconvention.impl.B2BTransformer;
import ar.com.kfgodel.transformbyconvention.impl.config.TransformerConfigurationByConvention;
import ar.com.kfgodel.webbyconvention.api.WebServer;
import ar.com.kfgodel.webbyconvention.api.config.WebServerConfiguration;
import ar.com.kfgodel.webbyconvention.impl.JettyWebServer;
import ar.com.kfgodel.webbyconvention.impl.config.ConfigurationByConvention;
import ar.com.tenpines.orm.api.HibernateOrm;
import ar.com.tenpines.orm.api.config.DbCoordinates;
import ar.com.tenpines.orm.impl.HibernateFacade;
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
  private ProceduresConfiguration config;
  private DependencyInjector injector;

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

  @Override
  public ProceduresConfiguration getConfiguration() {
    return config;
  }

  @Override
  public DependencyInjector getInjector() {
    return injector;
  }

  public static Application create(ProceduresConfiguration config) {
    ProceduresApplication application = new ProceduresApplication();
    application.config = config;
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
    this.injector = DependencyInjectorImpl.create();
    this.hibernate = createPersistenceLayer();
    this.injector.bindTo(HibernateOrm.class, this.hibernate);

    this.webServer = createWebServer(this.hibernate);
    this.transformer = createTransformer();
    registerCleanupHook();
  }

  private TypeTransformer createTransformer() {
    TransformerConfigurationByConvention configuration = TransformerConfigurationByConvention.create(this.getInjector());
    return B2BTransformer.create(configuration);
  }

  private void registerCleanupHook() {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      LOG.info("Cleaning-up before shutdown");
      this.stop();
    }, "cleanup-thread"));
  }

  private HibernateOrm createPersistenceLayer() {
    DbCoordinates dbCoordinates = config.getDatabaseCoordinates();
    HibernateOrm hibernateOrm = HibernateFacade.createWithConventionsFor(dbCoordinates);
    return hibernateOrm;
  }

  private WebServer createWebServer(HibernateOrm hibernateOrm) {
    WebServerConfiguration serverConfig = ConfigurationByConvention.create()
      .listeningHttpOn(config.getHttpPort())
      .withInjections((binder) -> {
        binder.bind(this).to(Application.class);
      })
      .authenticatingWith(DatabaseAuthenticator.create(hibernateOrm));
    return JettyWebServer.createFor(serverConfig);
  }

}
