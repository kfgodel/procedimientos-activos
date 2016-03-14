package ar.com.tenpines.html5poc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This type represents the configuration selector that bases its decition on the presence of heroku environment variables
 * to determine the best configuration
 * Created by kfgodel on 13/03/16.
 */
public class HerokuConfigSelector implements ConfigurationSelector {
  public static Logger LOG = LoggerFactory.getLogger(HerokuConfigSelector.class);

  public static HerokuConfigSelector create() {
    HerokuConfigSelector selector = new HerokuConfigSelector();
    return selector;
  }

  @Override
  public ProceduresConfiguration selectConfig() {
    if(System.getenv("PORT") != null && System.getenv("DATABASE_URL") != null){
      LOG.info("Using Heroku configuration");
      return HerokuConfig.create();
    }
    LOG.info("Using development configuration");
    return DevelopmentConfig.create();
  }
}
