package ar.com.tenpines.html5poc;

import ar.com.kfgodel.webbyconvention.WebServer;
import ar.com.tenpines.html5poc.components.transformer.TypeTransformer;
import ar.com.tenpines.orm.api.HibernateOrm;

/**
 * This type represents the contract that the application has to outside objects.<br>
 *   The application serves as the start and stop object, as well as the main access to application modules
 * Created by kfgodel on 03/03/16.
 */
public interface Application {

  /**
   * @return The web server that hosts the application requests and responses
   */
  WebServer getWebServerModule();

  /**
   * @return The orm layer backed by hibernate to interact with the relational database
   */
  HibernateOrm getOrmModule();

  /**
   * @return The transformer layer that can cnvert between different object types
   */
  TypeTransformer getTransformerModule();

  /**
   * Starts this application and its modules
   */
  void start();

  /**
   * Stops this application and its modules (freeing resources)
   */
  void stop();
}
