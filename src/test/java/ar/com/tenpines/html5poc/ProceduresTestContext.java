package ar.com.tenpines.html5poc;

import ar.com.dgarcia.javaspec.api.contexts.TestContext;
import ar.com.tenpines.html5poc.config.ConfigurationSelector;

import java.util.function.Supplier;

/**
 * This type represents the test context used for procedures tests
 * Created by kfgodel on 13/03/16.
 */
public interface ProceduresTestContext extends TestContext {

  ConfigurationSelector selector();
  void selector(Supplier<ConfigurationSelector> definition);

}
