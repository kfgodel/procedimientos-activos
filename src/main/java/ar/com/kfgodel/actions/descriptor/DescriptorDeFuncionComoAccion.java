package ar.com.kfgodel.actions.descriptor;

import ar.com.kfgodel.diamond.api.Diamond;
import ar.com.kfgodel.diamond.api.types.TypeInstance;

import javax.annotation.Resource;
import java.util.function.Function;

/**
 * This type describes a typed action that must be adapted to be used for json messages
 * <p>
 * Created by kfgodel on 12/11/16.
 */
public class DescriptorDeFuncionComoAccion {

  private Class<? extends Function> tipoDeAccion;
  private String nombreDeRecurso;
  private Class expectedInputType;

  public static DescriptorDeFuncionComoAccion create(Class<? extends Function> tipoDeAccion) {
    DescriptorDeFuncionComoAccion descriptor = new DescriptorDeFuncionComoAccion();
    descriptor.tipoDeAccion = tipoDeAccion;
    return descriptor;
  }

  public String getNombreDeRecurso() {
    if (nombreDeRecurso == null) {
      nombreDeRecurso = obtenerRecursoDe(tipoDeAccion);
    }
    return nombreDeRecurso;
  }

  private String obtenerRecursoDe(Class<? extends Function> tipoDeAccion) {
    Resource annotation = tipoDeAccion.getAnnotation(Resource.class);
    String expectedResourceName = annotation.name();
    return expectedResourceName;
  }


  public Class getExpectedInputType() {
    if (expectedInputType == null) {
      expectedInputType = getExpectedInputTypeFor(tipoDeAccion);
    }
    return expectedInputType;
  }

  /**
   * @param actionType
   * @return The class instance that represents the expected input type for this function
   */
  private Class<?> getExpectedInputTypeFor(Class<? extends Function> actionType) {
    TypeInstance typeArgument = Diamond.of(actionType)
      .inheritance()
      .typeLineage()
      .genericArgumentsOf(Diamond.of(Function.class))
      .findFirst().get();
    Class tipoNativo = (Class) typeArgument.nativeTypes().get();
    return tipoNativo;
  }

  public Class<? extends Function> getFunctionType() {
    return tipoDeAccion;
  }
}
