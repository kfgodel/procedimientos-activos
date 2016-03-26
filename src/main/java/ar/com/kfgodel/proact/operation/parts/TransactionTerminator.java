package ar.com.kfgodel.proact.operation.parts;

import ar.com.tenpines.orm.api.operations.TransactionOperation;

import java.util.function.Function;

/**
 * Created by kfgodel on 25/03/16.
 */
public interface TransactionTerminator<I1> extends ConversionTerminator<I1> {

  <O1> O1 applyResultOf(Function<I1, TransactionOperation<O1>> actionGenerator);
}
