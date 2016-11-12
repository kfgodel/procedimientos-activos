package convention.rest.api;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.Diamond;
import ar.com.kfgodel.diamond.api.types.TypeInstance;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.reflections.Reflections;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import java.util.*;
import java.util.function.Function;

/**
 * This type represents the resource to access procedures
 * Created by kfgodel on 17/03/15.
 */
@Produces("application/json")
@Consumes("application/json")
public class MessageResource {

  @Inject
  private DependencyInjector appInjector;
  private ObjectMapper objectMapper;
  private Map<String, Class<? extends Function>> tipoDeAccionPorId;


  @POST
  public Object processMessage(Map<String, Object> messageContent) {
    Class<? extends Function> actionType = buscarTipoDeAccionPara(messageContent);

    Object actionInput = prepareFromJsonFor(actionType, messageContent);
    Function action = appInjector.createInjected(actionType);
    Object actionResult = action.apply(actionInput);

    return prepareForJson(actionResult);
  }

  private Class<? extends Function> buscarTipoDeAccionPara(Map<String, Object> messageContent) {
    Object idDeAccionEnMensaje = messageContent.get("recurso");
    Optional<Class<? extends Function>> tipoDeAccion = Optional.of(getTipoDeAccionPorId().get(idDeAccionEnMensaje));
    return tipoDeAccion.orElseThrow(() -> new WebApplicationException("Accion no encontrada para el mensaje", 404));
  }

  public Map<String, Class<? extends Function>> getTipoDeAccionPorId() {
    if (tipoDeAccionPorId == null) {
      tipoDeAccionPorId = inicializarTiposDeAccion();
    }
    return tipoDeAccionPorId;
  }

  private Map<String, Class<? extends Function>> inicializarTiposDeAccion() {
    Map<String, Class<? extends Function>> tiposCondicionados = new HashMap<>();
    Set<Class<? extends Function>> tiposDeAccion = buscarTiposDeAccionDisponibles();
    tiposDeAccion.forEach((tipoDeAccion) -> {
      tiposCondicionados.put(obtenerIdPara(tipoDeAccion), tipoDeAccion);
    });
    return tiposCondicionados;
  }

  private Set<Class<? extends Function>> buscarTiposDeAccionDisponibles() {
    Reflections reflections = new Reflections("convention.action");
    Set<Class<? extends Function>> tiposDeFuncion = reflections.getSubTypesOf(Function.class);
    Set<Class<?>> tiposAnnotados = reflections.getTypesAnnotatedWith(Resource.class);
    Sets.SetView<Class<? extends Function>> tiposDeAccion = Sets.intersection(tiposDeFuncion, tiposAnnotados);
    return tiposDeAccion;
  }

  private String obtenerIdPara(Class<? extends Function> tipoDeAccion) {
    String expectedResourceName = extractResourceNameFrom(tipoDeAccion);
    return expectedResourceName;
  }

  private String extractResourceNameFrom(Class<? extends Function> tipoDeAccion) {
    Resource annotation = tipoDeAccion.getAnnotation(Resource.class);
    return annotation.name();
  }

  private <T> T prepareFromJsonFor(Class<? extends Function> actionType, Map<String, Object> messageContent) {
    Class<?> expectedInputType = getExpectedInputTypeFor(actionType);
    T fromJson = (T) getObjectMapper().convertValue(messageContent, expectedInputType);
    return fromJson;
  }

  private Object prepareForJson(Object actionResult) {
    JavaType expectedType;
    if (actionResult instanceof Collection) {
      // Use array like
      expectedType = getObjectMapper().getTypeFactory().constructCollectionType(List.class, Object.class);
    } else {
      // Normal object, use a hash
      expectedType = getObjectMapper().getTypeFactory().constructMapType(Map.class, String.class, Object.class);
    }
    Object jsonized = getObjectMapper().convertValue(actionResult, expectedType);
    return jsonized;
  }



  public static MessageResource create(DependencyInjector appInjector) {
    MessageResource resource = new MessageResource();
    resource.appInjector = appInjector;
    return resource;
  }

  private ObjectMapper getObjectMapper() {
    if (objectMapper == null) {
      objectMapper = new ObjectMapper();
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    return objectMapper;
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

}
