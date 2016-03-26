package ar.com.kfgodel.appbyconvention.operation.api;

import ar.com.kfgodel.appbyconvention.operation.api.chains.SessionScopedOperation;
import ar.com.kfgodel.appbyconvention.operation.api.chains.TransactionScopedOperation;
import ar.com.kfgodel.appbyconvention.operation.impl.ApplicationOperationNode;
import ar.com.kfgodel.transformbyconvention.api.TypeTransformer;
import ar.com.tenpines.orm.api.HibernateOrm;

/**
 * This type represents an operation that affects several application layers (usually persistence and others)
 * and that can be expressed as a chain of actions to do one after the other. Each action may involve a different
 * application module (such as persistence or transformer)
 * <p>
 * Created by kfgodel on 21/03/16.
 */
public interface ApplicationOperation {

  /**
   * Creates a new empty operation that waits to be defined. <br>
   * The client code should indicate the scope of the operation after its creation
   *
   * @param orm         The orm access object
   * @param transformer The type transformer
   * @return The created application operation
   */
  static ApplicationOperation createFor(HibernateOrm orm, TypeTransformer transformer) {
    return ApplicationOperationNode.create(orm, transformer);
  }

  /**
   * Indicates the scope of the operation to be executed inside a orm session.<br>
   * All actions chained to this operation will have access to the orm capabilities
   *
   * @return This operation with the scope defined
   */
  SessionScopedOperation insideASession();

  /**
   * Indicates the scope of the operation to be executed inside a orm transaction.<br>
   * All actions chained to this operation will have access to the orm capabilities
   *
   * @return This operation with the scope defined
   */
  TransactionScopedOperation insideATransaction();

}
