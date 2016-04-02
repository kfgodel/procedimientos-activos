package ar.com.kfgodel.proact.config;

import ar.com.kfgodel.orm.api.config.DbCoordinates;
import ar.com.kfgodel.orm.impl.config.ImmutableDbCoordinates;
import com.heroku.sdk.jdbc.DatabaseUrl;

import java.net.URISyntaxException;

/**
 * This type represents the configuration used to run in heroku with values taken from the OS environment
 * Created by kfgodel on 13/03/16.
 */
public class HerokuConfig implements ProceduresConfiguration {

  public static HerokuConfig create() {
    HerokuConfig config = new HerokuConfig();
    return config;
  }

  @Override
  public DbCoordinates getDatabaseCoordinates() {
    DatabaseUrl herokuCoordinates = null;
    try {
      herokuCoordinates = DatabaseUrl.extract();
    } catch (URISyntaxException e) {
      throw new RuntimeException("Error accessing heroku jdbc url",e);
    }
    String url = herokuCoordinates.jdbcUrl();
    String userName = herokuCoordinates.username();
    String password = herokuCoordinates.password();
    return ImmutableDbCoordinates.createDeductingDialect(url, userName, password);
  }

  @Override
  public int getHttpPort() {
    return Integer.valueOf(System.getenv("PORT"));
  }
}
