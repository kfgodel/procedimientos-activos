package ar.com.kfgodel.appbyconvention.operation.impl.chains;

import ar.com.kfgodel.appbyconvention.operation.api.chains.ChainedTransactionOperation;
import ar.com.kfgodel.appbyconvention.operation.api.chains.TransactionScopedOperation;
import ar.com.kfgodel.appbyconvention.operation.impl.comphelp.TransactionApplierFunction;
import ar.com.kfgodel.appbyconvention.operation.impl.comphelp.TransformerFunction;
import ar.com.tenpines.orm.api.operations.TransactionOperation;

import java.lang.reflect.Type;
import java.util.function.BiFunction;

/**
 * This type implements the session scoped operation by using two base functions
 * Created by kfgodel on 25/03/16.
 */
public class TransactionScopedOperationNode implements TransactionScopedOperation {

  private TransactionApplierFunction<?> applierFunction;
  private TransformerFunction<?> transformerFunction;

  public static TransactionScopedOperationNode create(TransactionApplierFunction<?> applierFunction, TransformerFunction<?> transformerFunction) {
    TransactionScopedOperationNode node = new TransactionScopedOperationNode();
    node.applierFunction = applierFunction;
    node.transformerFunction = transformerFunction;
    return node;
  }

  @Override
  public <O1> O1 apply(TransactionOperation<O1> action) {
    TransactionApplierFunction<O1> applierFunction = getApplierFunction();
    return applierFunction.apply(action);
  }

  @Override
  public <O1> ChainedTransactionOperation<O1> applying(TransactionOperation<O1> action) {
    return ChainedTransactionOperationNode.create(this, action);
  }

  public <I2> I2 transformTo(Type expectedType, Object previousValue) {
    BiFunction<Type, Object, I2> transformerFunction = getTransformerFunction();
    return transformerFunction.apply(expectedType, previousValue);
  }

  private <O1> TransactionApplierFunction<O1> getApplierFunction() {
    return (TransactionApplierFunction<O1>) applierFunction;
  }

  private <O1> TransformerFunction<O1> getTransformerFunction() {
    return (TransformerFunction<O1>) transformerFunction;
  }

}
