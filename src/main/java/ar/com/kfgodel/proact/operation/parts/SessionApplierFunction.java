package ar.com.kfgodel.proact.operation.parts;

import ar.com.tenpines.orm.api.operations.SessionOperation;

import java.util.function.Function;

/**
 * This type is needed to bind a session operation result type with the function result type and help the compiler
 * inferring types
 * Created by kfgodel on 25/03/16.
 */
public interface SessionApplierFunction<O1> extends Function<SessionOperation<O1>, O1> {
}
