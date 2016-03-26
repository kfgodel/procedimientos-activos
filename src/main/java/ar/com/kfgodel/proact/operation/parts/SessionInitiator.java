package ar.com.kfgodel.proact.operation.parts;

import ar.com.kfgodel.proact.operation.ChainedSessionOperation;
import ar.com.tenpines.orm.api.operations.SessionOperation;

import java.util.function.Supplier;

/**
 * This type represents a starting point for a session operation that can be chained with other operations.<br>
 * The methods in this type lead to a type that can chain more operations, or finish the chain
 * Created by kfgodel on 25/03/16.
 */
public interface SessionInitiator {

  <O1> O1 apply(SessionOperation<O1> action);

  <O1> ChainedSessionOperation<O1> applying(SessionOperation<O1> action);

  default <O1> O1 applyResultFrom(Supplier<SessionOperation<O1>> actionSupplier) {
    return applyingResultFrom(actionSupplier).get();
  }

  default <O1> ChainedSessionOperation<O1> applyingResultFrom(Supplier<SessionOperation<O1>> actionSupplier) {
    return applying((context) -> {
      SessionOperation<O1> action = actionSupplier.get();
      return action.applyWithSessionOn(context);
    });
  }

  default <I1> ChainedSessionOperation<I1> taking(I1 inputValue) {
    return applying((context) -> inputValue);
  }

  default <I1> ChainedSessionOperation<I1> takingResultFrom(Supplier<I1> inputValueSupplier) {
    return applying((context) -> inputValueSupplier.get());
  }

}
