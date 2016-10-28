package convention.action;

import ar.com.kfgodel.diamond.api.Diamond;
import ar.com.kfgodel.diamond.api.types.TypeInstance;

import java.util.function.Function;

/**
 * This type represents an action generated outside of this system, on some kind of frontend
 * Created by kfgodel on 27/10/16.
 */
public interface FrontendAction<I, O> extends Function<I, O> {

  /**
   * @return The class instance that represents the expected input type for this function
   */
  default Class<I> getExpectedInputType() {
    Class<?> concreteClass = this.getClass();
    TypeInstance typeArgument = Diamond.of(concreteClass)
      .inheritance()
      .typeLineage()
      .genericArgumentsOf(Diamond.of(FrontendAction.class))
      .findFirst().get();
    Class tipoNativo = (Class) typeArgument.nativeTypes().get();
    return tipoNativo;
  }
}
