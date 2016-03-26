package ar.com.kfgodel.proact.operation.parts;

import ar.com.kfgodel.proact.operation.ChainedSessionOperation;
import ar.com.tenpines.orm.api.operations.SessionOperation;

import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * Created by kfgodel on 25/03/16.
 */
public interface SessionContinuator<I1> {

  <O1> ChainedSessionOperation<O1> applyingResultOf(Function<I1, SessionOperation<O1>> actionGenerator);

  default <I2> ChainedSessionOperation<I2> convertingTo(Class<I2> expectedType) {
    return convertingTo((Type) expectedType);
  }

  <I2> ChainedSessionOperation<I2> convertingTo(Type expectedType);

  default <I2> ChainedSessionOperation<I2> mapping(Function<I1, I2> mapper) {
    return applyingResultOf((previousValue) -> {
      return (context) -> mapper.apply(previousValue);
    });
  }
}
