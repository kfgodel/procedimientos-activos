package convention.rest.api;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.proact.persistent.filters.procedures.ProceduresByTextPortionOrdByName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import convention.persistent.Procedure;
import convention.rest.api.tos.ListTo;
import convention.rest.api.tos.ProcedureFilterTo;
import convention.rest.api.tos.ProcedureTo;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import java.lang.reflect.Type;
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

  private static final Type LIST_OF_PROCEDURES_TO = new ReferenceOf<List<ProcedureTo>>() {
  }.getReferencedType();

  @POST
  public Map<String, Object> getAllEntities(Map<String, Object> messageContent) {
    ObjectMapper objectMapper = new ObjectMapper();
    ProcedureFilterTo filter = objectMapper.convertValue(messageContent, ProcedureFilterTo.class);

    String searchText = filter.getSearchText();
    List<Procedure> operationResult = createOperation()
      .insideASession()
      .applying(ProceduresByTextPortionOrdByName.create(Nary.ofNullable(searchText)))
      .convertTo(LIST_OF_PROCEDURES_TO);
    ListTo wrappedResult = ListTo.create(operationResult);

    MapType expectedType = objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
    Object jsonized = objectMapper.convertValue(wrappedResult, expectedType);
    return (Map<String, Object>) jsonized;
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
