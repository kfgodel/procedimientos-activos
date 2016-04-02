package ar.com.kfgodel.proact.config;

import ar.com.kfgodel.orm.api.config.DbCoordinates;

/**
 * This type represents the configuration data for the procedures application to work
 * Created by kfgodel on 12/03/16.
 */
public interface ProceduresConfiguration {

  /**
   * @return The database information to connect to it
   */
  DbCoordinates getDatabaseCoordinates();

  /**
   * @return The port number to use for the web server
   */
  int getHttpPort();
}
