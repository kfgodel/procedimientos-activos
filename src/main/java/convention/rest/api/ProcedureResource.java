package convention.rest.api;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.orm.api.operations.basic.DeleteById;
import ar.com.kfgodel.orm.api.operations.basic.FindById;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import ar.com.kfgodel.proact.persistent.filters.procedures.ProceduresByTextPortionOrdByName;
import convention.persistent.Procedure;
import convention.rest.api.tos.ProcedureTo;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

/**
 * This type represents the resource to access procedures
 * Created by kfgodel on 17/03/15.
 */
@Produces("application/json")
@Consumes("application/json")
public class ProcedureResource {

  @Inject
  private DependencyInjector appInjector;

  private static final Type LIST_OF_PROCEDURES_TO = new ReferenceOf<List<ProcedureTo>>() {
  }.getReferencedType();

  @GET
  public List<ProcedureTo> getAllEntities(@QueryParam("searchText") String searchText) {
    return createOperation()
      .insideASession()
      .applying(ProceduresByTextPortionOrdByName.create(Nary.ofNullable(searchText)))
      .convertTo(LIST_OF_PROCEDURES_TO);
  }

  private ApplicationOperation createOperation() {
    return ApplicationOperation.createFor(appInjector);
  }

  @POST
  public ProcedureTo createEntity() {
    return createOperation()
      .insideASession()
      .taking(Procedure.create("Procedimiento " + UUID.randomUUID(), "Sin descripción"))
      .applyingResultOf(Save::create)
      .convertTo(ProcedureTo.class);
  }

  @GET
  @Path("/{entityId}")
  public ProcedureTo getSingleEntity(@PathParam("entityId") Long procedureId) throws WebApplicationException{
    return createOperation()
      .insideASession()
      .applying(FindById.create(Procedure.class, procedureId))
      .mapping((foundProcedure) -> {
        // Answer 404 instead of null if it doesn't exist
        return foundProcedure.orElseThrowRuntime(() -> new WebApplicationException("procedure not found", 404));
      })
      .convertTo(ProcedureTo.class);
  }


  @PUT
  @Path("/{entityId}")
  public ProcedureTo editEntity(ProcedureTo newState, @PathParam("entityId") Long procedureId) {
    return createOperation()
      .insideATransaction()
      .taking(newState)
      .convertingTo(Procedure.class)
      .applyingResultOf((editedProcedure) -> {
        // Answer 404 instead of null if it doesn't exist
        if (editedProcedure == null) {
          throw new WebApplicationException("procedure not found", 404);
        }
        return Save.create(editedProcedure);
      })
      .convertTo(ProcedureTo.class);
  }

  @DELETE
  @Path("/{entityId}")
  public void deleteEntity(@PathParam("entityId") Long procedureId) {
    createOperation()
      .insideATransaction()
      .apply(DeleteById.create(Procedure.class, procedureId));
  }

  public static ProcedureResource create(DependencyInjector appInjector) {
    ProcedureResource resource = new ProcedureResource();
    resource.appInjector = appInjector;
    return resource;
  }

}
