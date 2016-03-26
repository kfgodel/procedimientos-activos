package ar.com.kfgodel.appbyconvention.operation.api.chains.session;

import ar.com.kfgodel.appbyconvention.operation.api.chains.ChainedSessionOperation;
import ar.com.tenpines.orm.api.operations.SessionOperation;

import java.util.function.Supplier;

/**
 * This type represents a starting point for a session scoped action chaining operation.<br>
 * The methods in this type start a chain of actions that will be executed on the same session
 * Created by kfgodel on 25/03/16.
 */
public interface SessionInitiator {

  /**
   * Applies the given action on a session returning its result.<br>
   * The given action is not chained but executed directly
   *
   * @param action The action to execute
   * @param <O1>   The expected result type
   * @return The result of teh action
   */
  <O1> O1 apply(SessionOperation<O1> action);

  /**
   * Applies the action given through a supplier on a session, returning its result. The supplier is executed in
   * the context of a session.<br>
   * The given action is not chained but executed directly
   *
   * @param actionSupplier The supplier of teh action to execute
   * @param <O1>           The type of expected action result
   * @return The result of teh action
   */
  default <O1> O1 applyResultFrom(Supplier<SessionOperation<O1>> actionSupplier) {
    return applyingResultFrom(actionSupplier).get();
  }


  /**
   * Starts an action chain indicating the first action to execute. Further actions on the chain can
   * use this action result as input.<br>
   * This method doesn't execute the action, starts a chain that will execute all its action once it's completed
   *
   * @param action The action to start the chain
   * @param <O1>   The expected action result type
   * @return The started chain operation
   */
  <O1> ChainedSessionOperation<O1> applying(SessionOperation<O1> action);

  /**
   * Starts an action chain with a supplier to provide the first action. The supplier will be executed in the context
   * of a session to get the action.<br>
   * This method doesn't execute the action, starts a chain that will execute all its action once it's completed
   *
   * @param actionSupplier The supplier to get the action
   * @param <O1>           The expected action result type
   * @return The started chain
   */
  default <O1> ChainedSessionOperation<O1> applyingResultFrom(Supplier<SessionOperation<O1>> actionSupplier) {
    return applying((context) -> {
      SessionOperation<O1> action = actionSupplier.get();
      return action.applyWithSessionOn(context);
    });
  }

  /**
   * Starts a chain by defining an initial input value. Further actions on the chain can use this value
   *
   * @param inputValue The value to use on the next action
   * @param <I1>       The type of input
   * @return The started chain
   */
  default <I1> ChainedSessionOperation<I1> taking(I1 inputValue) {
    return applying((context) -> inputValue);
  }

  /**
   * Starts a chain by defining an initial input value indirectly through a supplier. The supplier will be executed
   * in the context of the session to produce the value
   *
   * @param inputValueSupplier The value supplier to use as input
   * @param <I1>               The type of input value
   * @return The started chain
   */
  default <I1> ChainedSessionOperation<I1> takingResultFrom(Supplier<I1> inputValueSupplier) {
    return applying((context) -> inputValueSupplier.get());
  }

}
