package ar.com.kfgodel.proact.operation;

import ar.com.kfgodel.proact.operation.parts.SessionContinuator;
import ar.com.kfgodel.proact.operation.parts.SessionTerminator;
import ar.com.tenpines.orm.api.operations.SessionOperation;

import java.lang.reflect.Type;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by kfgodel on 25/03/16.
 */
public interface ChainedSessionOperation<O1> extends SessionContinuator<O1>, SessionTerminator<O1>, Supplier<O1> {

  @Override
  default <O2> O2 map(Function<O1, O2> mapper) {
    return mapping(mapper).get();
  }

  @Override
  default <O11> O11 applyResultOf(Function<O1, SessionOperation<O11>> actionGenerator) {
    return applyingResultOf(actionGenerator).get();
  }

  @Override
  default <O2> O2 convertTo(Class<O2> expectedType) {
    return convertingTo(expectedType).get();
  }

  @Override
  default <O2> O2 convertTo(Type expectedType) {
    ChainedSessionOperation<O2> chainedOperation = convertingTo(expectedType);
    return chainedOperation.get();
  }

}
