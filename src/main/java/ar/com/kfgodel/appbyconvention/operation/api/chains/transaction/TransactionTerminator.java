package ar.com.kfgodel.appbyconvention.operation.api.chains.transaction;

import ar.com.kfgodel.appbyconvention.operation.api.chains.common.ConversionTerminator;
import ar.com.tenpines.orm.api.operations.TransactionOperation;

import java.util.function.Function;

/**
 * This type represents a started transaction scoped chain that can end itself by defining a final action.<br>
 * The methods in this type allows you to end the chain and get its final result executing all the chained action in
 * the same transaction
 * <p>
 * Created by kfgodel on 25/03/16.
 */
public interface TransactionTerminator<I1> extends ConversionTerminator<I1> {

  /**
   * Adds the given action to the chain, as the final action, and executes the chain in the transaction.
   * Returns the chain result
   *
   * @param actionGenerator The funciton that uses the previous action result to generate the last action
   * @param <O1>            The type of expected function and chain result
   * @return The result of all the chaines actions
   */
  <O1> O1 applyResultOf(Function<I1, TransactionOperation<O1>> actionGenerator);
}
