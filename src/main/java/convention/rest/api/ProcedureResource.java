package convention.rest.api;

import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.proact.Application;
import ar.com.kfgodel.proact.persistent.filters.procedures.ProceduresByTextPortionOrdByName;
import ar.com.tenpines.orm.api.operations.basic.DeleteById;
import ar.com.tenpines.orm.api.operations.basic.FindById;
import ar.com.tenpines.orm.api.operations.basic.Save;
import convention.persistent.Procedure;
import convention.rest.api.tos.ProcedureTo;

import javax.ws.rs.*;
import java.lang.reflect.Type;
import java.util.List;

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
    Nary<Procedure> procedimientos = application.getOrmModule()
      .ensureSessionFor(ProceduresByTextPortionOrdByName.create(Nary.ofNullable(searchText)));

    List<ProcedureTo> proceduresTo = this.application.getTransformerModule().transformTo(LIST_OF_PROCEDURES_TO, procedimientos);

    return proceduresTo;
  }

  private ProcedureTo createTo(Procedure procedure) {
    return this.application.getTransformerModule().transformTo(ProcedureTo.class, procedure);
  }

  @POST
  public ProcedureTo createEntity() {
    Procedure newProcedure = Procedure.create("Procedimiento nn", "Sin descripción");

    application.getOrmModule().ensureSessionFor(Save.create(newProcedure));

    return createTo(newProcedure);
  }

  @GET
  @Path("/{entityId}")
  public ProcedureTo getSingleEntity(@PathParam("entityId") Long procedureId) {
    Nary<Procedure> procedure = application.getOrmModule().ensureSessionFor(FindById.create(Procedure.class, procedureId));
    return procedure
      .mapOptional(this::createTo)
      .orElseThrow(() -> new WebApplicationException("procedure not found", 404));
  }


  @PUT
  @Path("/{entityId}")
  public ProcedureTo editEntity(ProcedureTo newState, @PathParam("entityId") Long procedureId) {
    Procedure procedure = application.getOrmModule().ensureTransactionFor(context -> {
      Procedure editedProcedure = this.application.getTransformerModule().transformTo(Procedure.class, newState);
      if (editedProcedure == null) {
        throw new WebApplicationException("procedure not found", 404);
      }
      Save.create(editedProcedure).applyWithSessionOn(context);
      return editedProcedure;
    });

    return createTo(procedure);
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
