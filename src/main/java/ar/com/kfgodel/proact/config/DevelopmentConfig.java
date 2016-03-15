package ar.com.kfgodel.proact.config;

import ar.com.tenpines.orm.api.config.DbCoordinates;
import ar.com.tenpines.orm.impl.config.ImmutableDbCoordinates;

/**
 * This type represents the configuration for a development environment
 *
 * Created by kfgodel on 12/03/16.
 */
public class DevelopmentConfig implements ProceduresConfiguration {

  public static DevelopmentConfig create() {
    DevelopmentConfig config = new DevelopmentConfig();
    return config;
  }

  @Override
  public DbCoordinates getDatabaseCoordinates() {
    return ImmutableDbCoordinates.createDeductingDialect("jdbc:h2:file:./db/h2", "sa", "");
  }

  @Override
  public int getHttpPort() {
    return 9090;
  }
}
