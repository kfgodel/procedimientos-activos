package ar.com.kfgodel.appbyconvention.operation.impl.comphelp;

import java.lang.reflect.Type;
import java.util.function.BiFunction;

/**
 * This type is needed to bind an expected output type to a function result type and help the compiler inferring types
 * Created by kfgodel on 25/03/16.
 */
public interface TransformerFunction<O1> extends BiFunction<Type, Object, O1> {
}
