package ar.com.tenpines.html5poc.config;

/**
 * This type represents the object that knows wich configuration should be used in the current application
 * Created by kfgodel on 13/03/16.
 */
public interface ConfigurationSelector {
  /**
   * Picks the best configuration for the application given the current environment
   * @return The configuration to use
   */
  ProceduresConfiguration selectConfig();
}
