package convention.rest.api;

import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.proact.Application;
import ar.com.kfgodel.proact.persistent.filters.medicamentos.MedicamentosByTextPortionOrdByName;
import ar.com.tenpines.orm.api.operations.basic.DeleteById;
import ar.com.tenpines.orm.api.operations.basic.FindById;
import ar.com.tenpines.orm.api.operations.basic.Save;
import convention.persistent.Medicamento;
import convention.persistent.Procedure;
import convention.rest.api.tos.MedicamentoTo;

import javax.ws.rs.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

/**
 * This type represents the resource to access medicamentos
 * Created by kfgodel on 17/03/15.
 */
public class MedicamentoResource {

  private static final Type LIST_OF_MEDICAMENTOS_TO = new ReferenceOf<List<MedicamentoTo>>() {
  }.getReferencedType();
  private Application application;

  @GET
  public List<MedicamentoTo> getAllEntities(@QueryParam("searchText") String searchText) {
    Nary<Medicamento> medicamentos = application.getOrmModule()
      .ensureSessionFor(MedicamentosByTextPortionOrdByName.create(Nary.ofNullable(searchText)));

    List<MedicamentoTo> proceduresTo = this.application.getTransformerModule().transformTo(LIST_OF_MEDICAMENTOS_TO, medicamentos);

    return proceduresTo;
  }

  private MedicamentoTo createTo(Medicamento medicamento) {
    return this.application.getTransformerModule().transformTo(MedicamentoTo.class, medicamento);
  }

  @POST
  public MedicamentoTo createEntity() {
    Medicamento newEntity = Medicamento.create("Medicamento " + UUID.randomUUID(),
      "**Nombre comercial:**  \n" +
      "- \n\n\n" +
      "**Laboratorio:**  \n" +
        "- \n\n\n" +
      "**Indicaciones:**  \n" +
        "- \n\n\n" +
      "**Contraindicaciones:**  \n" +
        "- \n\n\n" +
      "**Droga(s):**  \n"+
        "- \n\n\n" +
      "**Dosis:**  \n"+
      "- \n\n\n" +
      "**Efectos adversos:**  \n"+
      "- \n\n\n" +
      "**Presentacion:**  \n"+
        "- \n\n\n" +
      "**Posologia:**  \n"+
        "- \n\n\n" +
      "**Vias de administracion:**  \n"+
        "- \n\n\n" +
      "**Ventajas:**  \n"+
        "- \n\n\n" +
      "**Especie:**  \n"+
        "- \n\n\n"
    );

    application.getOrmModule().ensureSessionFor(Save.create(newEntity));

    return createTo(newEntity);
  }

  @GET
  @Path("/{entityId}")
  public MedicamentoTo getSingleEntity(@PathParam("entityId") Long medicamentoId) {
    Nary<Medicamento> medicamento = application.getOrmModule().ensureSessionFor(FindById.create(Medicamento.class, medicamentoId));
    return medicamento
      .mapOptional(this::createTo)
      .orElseThrow(() -> new WebApplicationException("medicamento not found", 404));
  }


  @PUT
  @Path("/{entityId}")
  public MedicamentoTo editEntity(MedicamentoTo newState, @PathParam("entityId") Long medicamentoId) {
    Medicamento medicamento = application.getOrmModule().ensureTransactionFor(context -> {
      Medicamento editedMedicamento = this.application.getTransformerModule().transformTo(Medicamento.class, newState);
      if (editedMedicamento == null) {
        throw new WebApplicationException("Medicamento not found", 404);
      }
      Save.create(editedMedicamento).applyWithSessionOn(context);
      return editedMedicamento;
    });

    return createTo(medicamento);
  }

  @DELETE
  @Path("/{procedureId}")
  public void deleteProcedure(@PathParam("procedureId") Long procedureId) {
    application.getOrmModule().ensureSessionFor(DeleteById.create(Procedure.class, procedureId));
  }

  public static MedicamentoResource create(Application application) {
    MedicamentoResource resource = new MedicamentoResource();
    resource.application = application;
    return resource;
  }

}
