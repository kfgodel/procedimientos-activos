package ar.com.kfgodel.proact.operation.impl;

import ar.com.kfgodel.proact.operation.ChainedTransactionOperation;
import ar.com.tenpines.orm.api.operations.TransactionOperation;

import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * Created by kfgodel on 25/03/16.
 */
public class ChainedTransactionOperationNode<I1> implements ChainedTransactionOperation<I1> {

  private TransactionScopedOperationNode transactionScopedOperationNode;
  private TransactionOperation<I1> previousAction;

  public static <I1> ChainedTransactionOperationNode<I1> create(TransactionScopedOperationNode transactionScopedOperationNode, TransactionOperation<I1> action) {
    ChainedTransactionOperationNode<I1> node = new ChainedTransactionOperationNode<>();
    node.transactionScopedOperationNode = transactionScopedOperationNode;
    node.previousAction = action;
    return node;
  }

  @Override
  public <O1> ChainedTransactionOperationNode<O1> applyingResultOf(Function<I1, TransactionOperation<O1>> actionGenerator) {
    return ChainedTransactionOperationNode.create(transactionScopedOperationNode, adaptChainToOperation(actionGenerator));
  }

  @Override
  public <I2> ChainedTransactionOperation<I2> convertingTo(Type expectedType) {
    return mapping(getFunctionToTransformTo(expectedType));
  }

  @Override
  public I1 get() {
    return transactionScopedOperationNode.apply(previousAction);
  }

  private <O1> TransactionOperation<O1> adaptChainToOperation(Function<I1, TransactionOperation<O1>> actionGenerator) {
    return (context) -> {
      I1 previousValue = previousAction.applyWithTransactionOn(context);
      TransactionOperation<O1> nextOperation = actionGenerator.apply(previousValue);
      O1 nextValue = nextOperation.applyWithTransactionOn(context);
      return nextValue;
    };
  }

  private <I2> Function<I1, I2> getFunctionToTransformTo(Type expectedType) {
    return (previousValue) -> transactionScopedOperationNode.transformTo(expectedType, previousValue);
  }

}
