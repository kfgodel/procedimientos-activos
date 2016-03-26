package ar.com.kfgodel.proact.operation.parts;

import ar.com.tenpines.orm.api.operations.TransactionOperation;

import java.util.function.Function;

/**
 * This type is needed to bind a transaction operation result type with the function result type and help the compiler
 * inferring types
 * Created by kfgodel on 25/03/16.
 */
public interface TransactionApplierFunction<O1> extends Function<TransactionOperation<O1>, O1> {
}
