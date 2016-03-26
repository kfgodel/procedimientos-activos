package ar.com.kfgodel.proact.operation.parts;

import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * Created by kfgodel on 25/03/16.
 */
public interface ConversionTerminator<O1> {

  <O2> O2 convertTo(Class<O2> expectedType);

  <O2> O2 convertTo(Type expectedType);

  <O2> O2 map(Function<O1, O2> mapper);
}
