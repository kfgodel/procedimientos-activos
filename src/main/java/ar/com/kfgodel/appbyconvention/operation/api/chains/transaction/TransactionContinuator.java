package ar.com.kfgodel.appbyconvention.operation.api.chains.transaction;

import ar.com.kfgodel.appbyconvention.operation.api.chains.ChainedTransactionOperation;
import ar.com.tenpines.orm.api.operations.TransactionOperation;

import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * This type represents a started transaction scoped chain that can extend itself by adding more actions.<br>
 * The methods in this type allows you to extend the chain before ending it
 * Created by kfgodel on 25/03/16.
 */
public interface TransactionContinuator<I1> {

  /**
   * Adds another action to the chain by transforming the previous action result into a new action.<br>
   * When executed this chain will process action after action, taking the result of previous actions
   * and generating new ones using the given generator
   *
   * @param actionGenerator The function that can produce the next action to chain
   * @param <O1>            The type of expected action result
   * @return The next chain link
   */
  <O1> ChainedTransactionOperation<O1> applyingResultOf(Function<I1, TransactionOperation<O1>> actionGenerator);

  /**
   * Adds a conversion action into the chain that transforms the result of the previous action result
   * to an instance of the given class
   *
   * @param expectedType The class that represents the type of expected result after conversion
   * @param <I2>         The type of expected result
   * @return The next chain link
   */
  default <I2> ChainedTransactionOperation<I2> convertingTo(Class<I2> expectedType) {
    return convertingTo((Type) expectedType);
  }

  /**
   * Adds a conversion action into the chain that transforms the result of the previous action result
   * to an instance of the given generic type
   *
   * @param expectedType The type that represents the generic type of expected result after conversion
   * @param <I2>         The type of expected result
   * @return The next chain link
   */
  <I2> ChainedTransactionOperation<I2> convertingTo(Type expectedType);

  /**
   * Adds an arbitrary mapping action to the chain that will transform the result of the previous action into another
   * value
   *
   * @param mapper The mapping function
   * @param <I2>   The type of function result
   * @return The next chain link
   */
  default <I2> ChainedTransactionOperation<I2> mapping(Function<I1, I2> mapper) {
    return applyingResultOf((previousValue) -> {
      return (context) -> mapper.apply(previousValue);
    });
  }
}
