package ar.com.kfgodel.proact.operation.parts;

import ar.com.kfgodel.proact.operation.ChainedTransactionOperation;
import ar.com.tenpines.orm.api.operations.TransactionOperation;

import java.util.function.Supplier;

/**
 * Created by kfgodel on 25/03/16.
 */
public interface TransactionInitiator {

  <O1> O1 apply(TransactionOperation<O1> action);

  <O1> ChainedTransactionOperation<O1> applying(TransactionOperation<O1> action);

  default <O1> O1 applyResultFrom(Supplier<TransactionOperation<O1>> actionSupplier) {
    return applyingResultFrom(actionSupplier).get();
  }

  default <O1> ChainedTransactionOperation<O1> applyingResultFrom(Supplier<TransactionOperation<O1>> actionSupplier) {
    return applying((context) -> {
      TransactionOperation<O1> action = actionSupplier.get();
      return action.applyWithTransactionOn(context);
    });
  }

  default <I1> ChainedTransactionOperation<I1> taking(I1 inputValue) {
    return applying((context) -> inputValue);
  }

  default <I1> ChainedTransactionOperation<I1> takingResultFrom(Supplier<I1> inputValue) {
    return applying((context) -> inputValue.get());
  }

}
