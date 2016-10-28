package convention.rest.api;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import convention.action.FindProceduresAction;
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

  @POST
  public Object getAllEntities(Map<String, Object> messageContent) {
    ObjectMapper objectMapper = new ObjectMapper();
    ProcedureFilterTo filter = objectMapper.convertValue(messageContent, ProcedureFilterTo.class);

    FindProceduresAction action = FindProceduresAction.create(appInjector);
    List<Procedure> actionResult = action.apply(filter);

    JavaType expectedType;
    if (actionResult instanceof Collection) {
      // Use array like
      expectedType = objectMapper.getTypeFactory().constructCollectionType(List.class, Object.class);
    } else {
      // Normal object, use a hash
      expectedType = objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
    }
    Object jsonized = objectMapper.convertValue(actionResult, expectedType);
    return jsonized;
  }

  private ApplicationOperation createOperation() {
    return ApplicationOperation.createFor(appInjector);
  }

  public static MessageResource create(DependencyInjector appInjector) {
    MessageResource resource = new MessageResource();
    resource.appInjector = appInjector;
    return resource;
  }

}
