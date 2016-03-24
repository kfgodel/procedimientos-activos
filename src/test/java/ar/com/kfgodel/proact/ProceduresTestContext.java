package ar.com.kfgodel.proact;

import ar.com.dgarcia.javaspec.api.TestContext;
import ar.com.kfgodel.proact.config.ConfigurationSelector;

import java.util.function.Supplier;

/**
 * This type represents the test context used for procedures tests
 * Created by kfgodel on 13/03/16.
 */
public interface ProceduresTestContext extends TestContext {

  ConfigurationSelector selector();
  void selector(Supplier<ConfigurationSelector> definition);

}
