package ar.com.kfgodel.appbyconvention.operation.impl;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.appbyconvention.operation.api.chains.SessionScopedOperation;
import ar.com.kfgodel.appbyconvention.operation.api.chains.TransactionScopedOperation;
import ar.com.kfgodel.appbyconvention.operation.impl.chains.SessionScopedOperationNode;
import ar.com.kfgodel.appbyconvention.operation.impl.chains.TransactionScopedOperationNode;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.transformbyconvention.api.TypeTransformer;
import ar.com.tenpines.orm.api.HibernateOrm;

/**
 * This type implements the application operation based on the application injector to retrieve the dependencies
 *
 * Created by kfgodel on 25/03/16.
 */
public class ApplicationOperationNode implements ApplicationOperation {

  private HibernateOrm orm;
  private TypeTransformer transformer;
  private DependencyInjector injector;

  @Override
  public SessionScopedOperation insideASession() {
    return SessionScopedOperationNode.create(getOrm()::ensureSessionFor, getTransformer()::transformTo);
  }

  @Override
  public TransactionScopedOperation insideATransaction() {
    return TransactionScopedOperationNode.create(getOrm()::ensureTransactionFor, getTransformer()::transformTo);
  }

  public static ApplicationOperationNode create(DependencyInjector injector) {
    ApplicationOperationNode node = new ApplicationOperationNode();
    node.injector = injector;
    return node;
  }

  private HibernateOrm getOrm() {
    if (orm == null) {
      orm = injector.getImplementationFor(HibernateOrm.class);
    }
    return orm;
  }

  private TypeTransformer getTransformer() {
    if (transformer == null) {
      transformer = injector.getImplementationFor(TypeTransformer.class);
    }
    return transformer;
  }
}
