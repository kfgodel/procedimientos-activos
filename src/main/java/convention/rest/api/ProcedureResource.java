package convention.rest.api;

import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.proact.Application;
import ar.com.kfgodel.proact.persistent.filters.procedures.ProceduresByTextPortionOrdByName;
import ar.com.kfgodel.transformbyconvention.api.TypeTransformer;
import ar.com.tenpines.orm.api.operations.basic.DeleteById;
import ar.com.tenpines.orm.api.operations.basic.Save;
import convention.persistent.Procedure;
import convention.rest.api.tos.ProcedureTo;

import javax.ws.rs.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * This type represents the resource to access procedures
 * Created by kfgodel on 17/03/15.
 */
public class ProcedureResource {

  private static final Type LIST_OF_PROCEDURES_TO = new ReferenceOf<List<ProcedureTo>>() {
  }.getReferencedType();
  private Application application;

  @GET
  public List<ProcedureTo> getAllEntities(@QueryParam("searchText") String searchText) {
    return application.getOrmModule().ensureSessionFor((context)->{
      // Declared function explicitly because compiler can't deduct type when using Type instances (LIST_OF_TOS)
      Function<String, List<ProcedureTo>> adaptedOperation = getTransformer()
        .adaptingIo(String.class, (text) ->
          ProceduresByTextPortionOrdByName.create(Nary.ofNullable(text)).applyWithSessionOn(context),
          LIST_OF_PROCEDURES_TO);
      return  adaptedOperation
        .apply(searchText);
    });
  }

  private ProcedureTo createTo(Procedure procedure) {
    return getTransformer().transformTo(ProcedureTo.class, procedure);
  }

  @POST
  public ProcedureTo createEntity() {
    return application.getOrmModule().ensureSessionFor((context)->
      getTransformer()
      .adaptingIo(Void.class, (nothing)->{
        Procedure newProcedure = Procedure.create("Procedimiento " + UUID.randomUUID(), "Sin descripción");
        Save.create(newProcedure).applyWithSessionOn(context);
        return newProcedure;
      }, ProcedureTo.class)
      .apply(null)
    );
  }

  @GET
  @Path("/{entityId}")
  public ProcedureTo getSingleEntity(@PathParam("entityId") Long procedureId) throws WebApplicationException{
    return application.getOrmModule().ensureSessionFor((context) ->
      getTransformer()
      .adaptingIo(Procedure.class, (procedure) -> {
          if(procedure == null){
            throw new WebApplicationException("procedure not found", 404);
          }
          return procedure;
        },
        ProcedureTo.class)
      .apply(procedureId)
    );
  }


  @PUT
  @Path("/{entityId}")
  public ProcedureTo editEntity(ProcedureTo newState, @PathParam("entityId") Long procedureId) {
    return application.getOrmModule().ensureTransactionFor((context)->
      getTransformer()
      .adaptingIo(Procedure.class, (editedProcedure)->{
        if (editedProcedure == null) {
          throw new WebApplicationException("procedure not found", 404);
        }
        Save.create(editedProcedure).applyWithSessionOn(context);
        return editedProcedure;
      }, ProcedureTo.class)
      .apply(newState)
    );
  }

  private TypeTransformer getTransformer() {
    return this.application.getTransformerModule();
  }

  @DELETE
  @Path("/{entityId}")
  public void deleteEntity(@PathParam("entityId") Long procedureId) {
    application.getOrmModule().ensureTransactionFor(DeleteById.create(Procedure.class, procedureId));
  }

  public static ProcedureResource create(Application application) {
    ProcedureResource resource = new ProcedureResource();
    resource.application = application;
    return resource;
  }

}
