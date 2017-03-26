package ar.com.kfgodel.actions.descriptor;

import ar.com.kfgodel.nary.api.optionals.Optional;
import com.google.common.collect.Sets;
import org.reflections.Reflections;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;

/**
 * This type represents the object that knows how to find the best action type for a given message
 * Created by kfgodel on 12/11/16.
 */
public class BuscadorDeFuncionesTipoAccion {

  public static final String KEY_QUE_INDICA_ACCION = "recurso";
  public static final String PAQUETE_DE_ACCIONES = "convention.action";

  private Map<String, Class<? extends Function>> tipoDeAccionPorRecurso;

  public static BuscadorDeFuncionesTipoAccion create() {
    BuscadorDeFuncionesTipoAccion buscadorDeFuncionesTipoAccion = new BuscadorDeFuncionesTipoAccion();
    buscadorDeFuncionesTipoAccion.inicializar();
    return buscadorDeFuncionesTipoAccion;
  }

  private void inicializar() {
    this.tipoDeAccionPorRecurso = this.descubrirAccionesPorRecursoEnClasspath();
  }

  private Map<String, Class<? extends Function>> descubrirAccionesPorRecursoEnClasspath() {
    Map<String, Class<? extends Function>> accionPorRecurso = new HashMap<>();
    Set<Class<? extends Function>> tiposDeAccion = buscarTiposDeAccionDisponibles();
    tiposDeAccion.forEach((tipoDeAccion) -> {
      String recursoAsociado = obtenerRecursoDe(tipoDeAccion);
      accionPorRecurso.put(recursoAsociado, tipoDeAccion);
    });
    return accionPorRecurso;
  }

  private Set<Class<? extends Function>> buscarTiposDeAccionDisponibles() {
    Reflections reflections = new Reflections(PAQUETE_DE_ACCIONES);
    Set<Class<? extends Function>> tiposDeFuncion = reflections.getSubTypesOf(Function.class);
    Set<Class<?>> anotadosConResource = reflections.getTypesAnnotatedWith(Resource.class);
    Sets.SetView<Class<? extends Function>> tiposDeAccion = Sets.intersection(tiposDeFuncion, anotadosConResource);
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

  public Optional<Class<? extends Function>> buscarMejorTipoDeAccionPara(Map<String, Object> mensaje) {
    Object idDeAccionEnMensaje = mensaje.get(KEY_QUE_INDICA_ACCION);
    if(idDeAccionEnMensaje == null){
      throw new IllegalArgumentException("El mensaje no contiene la key["+KEY_QUE_INDICA_ACCION+"] esperada: " + mensaje);
    }
    Class<? extends Function> tipoEncontrado = tipoDeAccionPorRecurso.get(idDeAccionEnMensaje);
    return Optional.ofNullable(tipoEncontrado);
  }
}
