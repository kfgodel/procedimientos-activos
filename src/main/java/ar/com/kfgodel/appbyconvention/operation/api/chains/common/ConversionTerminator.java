package ar.com.kfgodel.appbyconvention.operation.api.chains.common;

import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * This type represents a started action chain that can end itself by defining a final action.<br>
 * The methods in this type allows you to end the chain and get its final result executing all the chained action in
 * the same scope
 * <p>
 * Created by kfgodel on 25/03/16.
 */
public interface ConversionTerminator<O1> {

  /**
   * Adds a conversion from the previous action result as the final action into the chain and executes the whole chain
   * to get its result
   *
   * @param expectedType The class that represents the expected type after the conversion
   * @param <O2>         The type of the chain result
   * @return The chain result
   */
  <O2> O2 convertTo(Class<O2> expectedType);

  /**
   * Adds a conversion from the previous action result as the final action into the chain and executes the whole chain
   * to get its result
   *
   * @param expectedType The type that represents the expected generic type after the conversion
   * @param <O2>         The type of the chain result
   * @return The chain result
   */
  <O2> O2 convertTo(Type expectedType);

  /**
   * Adds an arbitrary map from the previous action result as the final action in the chain.
   * Executes the whole chain to get its result.
   *
   * @param mapper The function to map the final result
   * @param <O2>   The type of expected function and chain result
   * @return The result of teh chain
   */
  <O2> O2 map(Function<O1, O2> mapper);
}
