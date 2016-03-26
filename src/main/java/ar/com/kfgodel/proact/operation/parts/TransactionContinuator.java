package ar.com.kfgodel.proact.operation.parts;

import ar.com.kfgodel.proact.operation.ChainedTransactionOperation;
import ar.com.tenpines.orm.api.operations.TransactionOperation;

import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * Created by kfgodel on 25/03/16.
 */
public interface TransactionContinuator<I1> {

  <O1> ChainedTransactionOperation<O1> applyingResultOf(Function<I1, TransactionOperation<O1>> actionGenerator);

  default <I2> ChainedTransactionOperation<I2> convertingTo(Class<I2> expectedType) {
    return convertingTo((Type) expectedType);
  }

  <I2> ChainedTransactionOperation<I2> convertingTo(Type expectedType);

  default <I2> ChainedTransactionOperation<I2> mapping(Function<I1, I2> mapper) {
    return applyingResultOf((previousValue) -> {
      return (context) -> mapper.apply(previousValue);
    });
  }
}
