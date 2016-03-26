package ar.com.kfgodel.proact.operation.impl;

import ar.com.kfgodel.proact.operation.ChainedSessionOperation;
import ar.com.kfgodel.proact.operation.SessionScopedOperation;
import ar.com.kfgodel.proact.operation.parts.SessionApplierFunction;
import ar.com.kfgodel.proact.operation.parts.TransformerFunction;
import ar.com.tenpines.orm.api.operations.SessionOperation;

import java.lang.reflect.Type;
import java.util.function.BiFunction;

/**
 * Created by kfgodel on 25/03/16.
 */
public class SessionScopedOperationNode implements SessionScopedOperation {

  private SessionApplierFunction<?> applierFunction;
  private TransformerFunction<?> transformerFunction;

  public static SessionScopedOperationNode create(SessionApplierFunction<?> applierFunction, TransformerFunction<?> transformerFunction) {
    SessionScopedOperationNode node = new SessionScopedOperationNode();
    node.applierFunction = applierFunction;
    node.transformerFunction = transformerFunction;
    return node;
  }

  @Override
  public <O1> O1 apply(SessionOperation<O1> action) {
    SessionApplierFunction<O1> applierFunction = getApplierFunction();
    return applierFunction.apply(action);
  }

  @Override
  public <O1> ChainedSessionOperation<O1> applying(SessionOperation<O1> action) {
    return ChainedSessionOperationNode.create(this, action);
  }

  public <I2> I2 transformTo(Type expectedType, Object previousValue) {
    BiFunction<Type, Object, I2> transformerFunction = getTransformerFunction();
    return transformerFunction.apply(expectedType, previousValue);
  }

  private <O1> SessionApplierFunction<O1> getApplierFunction() {
    return (SessionApplierFunction<O1>) applierFunction;
  }

  private <O1> TransformerFunction<O1> getTransformerFunction() {
    return (TransformerFunction<O1>) transformerFunction;
  }


}
