package ar.com.kfgodel.proact.operation.parts;

import ar.com.tenpines.orm.api.operations.SessionOperation;

import java.util.function.Function;

/**
 * Created by kfgodel on 25/03/16.
 */
public interface SessionTerminator<I1> extends ConversionTerminator<I1> {

  <O1> O1 applyResultOf(Function<I1, SessionOperation<O1>> actionGenerator);

}
