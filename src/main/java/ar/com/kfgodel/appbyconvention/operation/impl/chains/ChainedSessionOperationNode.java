package ar.com.kfgodel.appbyconvention.operation.impl.chains;

import ar.com.kfgodel.appbyconvention.operation.api.chains.ChainedSessionOperation;
import ar.com.tenpines.orm.api.operations.SessionOperation;

import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * This type implements the chained session operation by wrapping the previous action
 * Created by kfgodel on 25/03/16.
 */
public class ChainedSessionOperationNode<I1> implements ChainedSessionOperation<I1> {

  private SessionScopedOperationNode sessionScopedOperationNode;
  private SessionOperation<I1> previousAction;

  public static <I1> ChainedSessionOperationNode<I1> create(SessionScopedOperationNode sessionScopedOperationNode, SessionOperation<I1> action) {
    ChainedSessionOperationNode<I1> node = new ChainedSessionOperationNode<>();
    node.sessionScopedOperationNode = sessionScopedOperationNode;
    node.previousAction = action;
    return node;
  }

  @Override
  public <O1> ChainedSessionOperation<O1> applyingResultOf(Function<I1, SessionOperation<O1>> actionGenerator) {
    return ChainedSessionOperationNode.create(sessionScopedOperationNode, adaptChainToOperation(actionGenerator));
  }

  @Override
  public <I2> ChainedSessionOperation<I2> convertingTo(Type expectedType) {
    return mapping(getFunctionToTransformTo(expectedType));
  }

  @Override
  public I1 get() {
    return sessionScopedOperationNode.apply(previousAction);
  }

  private <O1> SessionOperation<O1> adaptChainToOperation(Function<I1, SessionOperation<O1>> actionGenerator) {
    return (context) -> {
      I1 previousValue = previousAction.applyWithSessionOn(context);
      SessionOperation<O1> nextOperation = actionGenerator.apply(previousValue);
      O1 nextValue = nextOperation.applyWithSessionOn(context);
      return nextValue;
    };
  }

  private <I2> Function<I1, I2> getFunctionToTransformTo(Type expectedType) {
    return (previousValue) -> sessionScopedOperationNode.transformTo(expectedType, previousValue);
  }

}
