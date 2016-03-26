package ar.com.kfgodel.appbyconvention.operation.impl;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.appbyconvention.operation.api.chains.SessionScopedOperation;
import ar.com.kfgodel.appbyconvention.operation.api.chains.TransactionScopedOperation;
import ar.com.kfgodel.appbyconvention.operation.impl.chains.SessionScopedOperationNode;
import ar.com.kfgodel.appbyconvention.operation.impl.chains.TransactionScopedOperationNode;
import ar.com.kfgodel.transformbyconvention.api.TypeTransformer;
import ar.com.tenpines.orm.api.HibernateOrm;

/**
 * This type implements the application operation based on application modules
 * Created by kfgodel on 25/03/16.
 */
public class ApplicationOperationNode implements ApplicationOperation {

  private HibernateOrm orm;
  private TypeTransformer transformer;

  @Override
  public SessionScopedOperation insideASession() {
    return SessionScopedOperationNode.create(getOrm()::ensureSessionFor, getTransformer()::transformTo);
  }

  @Override
  public TransactionScopedOperation insideATransaction() {
    return TransactionScopedOperationNode.create(getOrm()::ensureTransactionFor, getTransformer()::transformTo);
  }

  public static ApplicationOperationNode create(HibernateOrm orm, TypeTransformer transformer) {
    ApplicationOperationNode node = new ApplicationOperationNode();
    node.orm = orm;
    node.transformer = transformer;
    return node;
  }

  private HibernateOrm getOrm() {
    return orm;
  }

  private TypeTransformer getTransformer() {
    return transformer;
  }
}
