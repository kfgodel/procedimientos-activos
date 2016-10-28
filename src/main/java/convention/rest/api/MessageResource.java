package convention.rest.api;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import convention.action.FindProceduresAction;
import convention.action.FrontendAction;
import convention.persistent.Procedure;
import convention.rest.api.tos.ProcedureFilterTo;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

  @POST
  public Object getAllEntities(Map<String, Object> messageContent) {
    FindProceduresAction action = FindProceduresAction.create(appInjector);

    ProcedureFilterTo filter = prepareFromJson(messageContent, action);

    List<Procedure> actionResult = action.apply(filter);

    return prepareForJson(actionResult);
  }

  private <T> T prepareFromJson(Map<String, Object> messageContent, FrontendAction action) {
    Class<?> expectedInputType = action.getExpectedInputType();
    return (T) getObjectMapper().convertValue(messageContent, expectedInputType);
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
    }
    return objectMapper;
  }
}
