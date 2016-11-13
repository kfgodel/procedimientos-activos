package ar.com.kfgodel.actions.descriptor;

import ar.com.kfgodel.nary.api.optionals.Optional;
import com.google.common.collect.Sets;
import org.reflections.Reflections;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;

/**
 * This type represents the object that know how to find the best action type for a given message
 * Created by kfgodel on 12/11/16.
 */
public class BuscadorDeFuncionesTipoAccion {

  public static final String RECURSO_KEY = "recurso";
  public static final String ACTION_PACKAGE = "convention.action";

  private Map<String, Class<? extends Function>> tipoDeAccionPorRecurso;

  public static BuscadorDeFuncionesTipoAccion create() {
    BuscadorDeFuncionesTipoAccion buscadorDeFuncionesTipoAccion = new BuscadorDeFuncionesTipoAccion();
    buscadorDeFuncionesTipoAccion.inicializar();
    return buscadorDeFuncionesTipoAccion;
  }

  private void inicializar() {
    this.tipoDeAccionPorRecurso = this.descrubirAccionesEnClasspath();
  }

  private Map<String, Class<? extends Function>> descrubirAccionesEnClasspath() {
    Map<String, Class<? extends Function>> tiposCondicionados = new HashMap<>();
    Set<Class<? extends Function>> tiposDeAccion = buscarTiposDeAccionDisponibles();
    tiposDeAccion.forEach((tipoDeAccion) -> {
      tiposCondicionados.put(obtenerRecursoDe(tipoDeAccion), tipoDeAccion);
    });
    return tiposCondicionados;
  }

  private Set<Class<? extends Function>> buscarTiposDeAccionDisponibles() {
    Reflections reflections = new Reflections(ACTION_PACKAGE);
    Set<Class<? extends Function>> tiposDeFuncion = reflections.getSubTypesOf(Function.class);
    Set<Class<?>> tiposAnnotados = reflections.getTypesAnnotatedWith(Resource.class);
    Sets.SetView<Class<? extends Function>> tiposDeAccion = Sets.intersection(tiposDeFuncion, tiposAnnotados);
    return tiposDeAccion;
  }

  private String obtenerRecursoDe(Class<? extends Function> tipoDeAccion) {
    Resource annotation = tipoDeAccion.getAnnotation(Resource.class);
    String expectedResourceName = annotation.name();
    return expectedResourceName;
  }


  public List<Class<? extends Function>> allTiposDeAccion() {
    return new ArrayList<>(tipoDeAccionPorRecurso.values());
  }

  public Optional<Class<? extends Function>> buscarTipoPara(Map<String, Object> mensaje) {
    Object idDeAccionEnMensaje = mensaje.get(RECURSO_KEY);
    Class<? extends Function> tipoEncontrado = tipoDeAccionPorRecurso.get(idDeAccionEnMensaje);
    return Optional.ofNullable(tipoEncontrado);
  }
}
