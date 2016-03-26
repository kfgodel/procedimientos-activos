package ar.com.kfgodel.proact.operation;

import ar.com.kfgodel.proact.operation.impl.ApplicationOperationNode;
import ar.com.kfgodel.transformbyconvention.api.TypeTransformer;
import ar.com.tenpines.orm.api.HibernateOrm;

/**
 * This type represents an operation that affects several application layers (usually persistence and others)
 * Created by kfgodel on 21/03/16.
 */
public interface ApplicationOperation {

  static ApplicationOperation createFor(HibernateOrm orm, TypeTransformer transformer) {
    return ApplicationOperationNode.create(orm, transformer);
  }

  SessionScopedOperation insideASession();

  TransactionScopedOperation insideATransaction();

}
