package ar.com.kfgodel.actions;

import ar.com.kfgodel.actions.adapter.AdaptadorDeFuncionEnAccion;
import ar.com.kfgodel.actions.descriptor.DescriptorDeFuncionComoAccion;
import com.google.common.collect.Sets;
import org.reflections.Reflections;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This type allows finding frontend actions on the classpath
 * Created by kfgodel on 12/11/16.
 */
public class BuscadorDeAccionesEnClasspath {
  public static final String PACKAGE_CON_ACCIONES = "convention.action";

  private AdaptadorDeFuncionEnAccion adaptador;

  public static BuscadorDeAccionesEnClasspath create(AdaptadorDeFuncionEnAccion adaptador) {
    BuscadorDeAccionesEnClasspath buscador = new BuscadorDeAccionesEnClasspath();
    buscador.adaptador = adaptador;
    return buscador;
  }

  public List<FrontendAction> buscarAccionesDisponibles() {
    return buscarFuncionesAnotadasEnClasspath().stream()
      .map(DescriptorDeFuncionComoAccion::create)
      .map(adaptador::adaptar)
      .collect(Collectors.toList());
  }

  private Set<Class<? extends Function>> buscarFuncionesAnotadasEnClasspath() {
    Reflections reflections = new Reflections(PACKAGE_CON_ACCIONES);
    Set<Class<? extends Function>> tiposDeFuncion = reflections.getSubTypesOf(Function.class);
    Set<Class<?>> anotadosConResource = reflections.getTypesAnnotatedWith(Resource.class);
    Set<Class<? extends Function>> tiposDeAccion = Sets.intersection(tiposDeFuncion, anotadosConResource);
    return tiposDeAccion;
  }


}
