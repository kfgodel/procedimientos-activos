package convention.rest.api;

import ar.com.kfgodel.actions.BuscadorDeTipoDeAccion;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.Diamond;
import ar.com.kfgodel.diamond.api.types.TypeInstance;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
  @Inject
  private BuscadorDeTipoDeAccion buscadorDeTipo;
  private ObjectMapper objectMapper;


  @POST
  public Object processMessage(Map<String, Object> messageContent) {
    Class<? extends Function> actionType = buscadorDeTipo.buscarTipoPara(messageContent)
      .orElseThrow(() -> new WebApplicationException("Accion no encontrada para el mensaje", 404));

    Object actionInput = prepareFromJsonFor(actionType, messageContent);
    Function action = appInjector.createInjected(actionType);
    Object actionResult = action.apply(actionInput);

    return prepareForJson(actionResult);
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
