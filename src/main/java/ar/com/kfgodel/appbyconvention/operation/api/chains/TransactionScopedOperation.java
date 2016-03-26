package ar.com.kfgodel.appbyconvention.operation.api.chains;

import ar.com.kfgodel.appbyconvention.operation.api.chains.transaction.TransactionInitiator;

/**
 * This type represents an application operation scoped to a transaction. All actions chained to this operation
 * will be executed on inside the same transaction
 * <p>
 * Created by kfgodel on 21/03/16.
 */
public interface TransactionScopedOperation extends TransactionInitiator {

}
