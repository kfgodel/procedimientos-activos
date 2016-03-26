package ar.com.kfgodel.proact.operation.impl;

import ar.com.kfgodel.proact.operation.ApplicationOperation;
import ar.com.kfgodel.proact.operation.SessionScopedOperation;
import ar.com.kfgodel.proact.operation.TransactionScopedOperation;
import ar.com.kfgodel.transformbyconvention.api.TypeTransformer;
import ar.com.tenpines.orm.api.HibernateOrm;

/**
 * Created by kfgodel on 25/03/16.
 */
public class ApplicationOperationNode implements ApplicationOperation {

  private HibernateOrm orm;
  private TypeTransformer transformer;

  @Override
  public SessionScopedOperation insideASession() {
    return SessionScopedOperationNode.create(orm::ensureSessionFor, transformer::transformTo);
  }

  @Override
  public TransactionScopedOperation insideATransaction() {
    return TransactionScopedOperationNode.create(orm::ensureTransactionFor, transformer::transformTo);
  }

  public static ApplicationOperationNode create(HibernateOrm orm, TypeTransformer transformer) {
    ApplicationOperationNode node = new ApplicationOperationNode();
    node.orm = orm;
    node.transformer = transformer;
    return node;
  }

}
