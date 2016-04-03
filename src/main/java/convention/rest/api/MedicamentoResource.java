package convention.rest.api;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.orm.api.operations.basic.DeleteById;
import ar.com.kfgodel.orm.api.operations.basic.FindById;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import ar.com.kfgodel.proact.persistent.filters.medicamentos.MedicamentosByTextPortionOrdByName;
import convention.persistent.Medicamento;
import convention.rest.api.tos.MedicamentoTo;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

/**
 * This type represents the resource to access medicamentos
 * Created by kfgodel on 17/03/15.
 */
public class MedicamentoResource {

  @Inject
  private DependencyInjector appInjector;

  private static final Type LIST_OF_MEDICAMENTOS_TO = new ReferenceOf<List<MedicamentoTo>>() {
  }.getReferencedType();

  @GET
  public List<MedicamentoTo> getAllEntities(@QueryParam("searchText") String searchText) {
    return createOperation()
      .insideASession()
      .applying(MedicamentosByTextPortionOrdByName.create(Nary.ofNullable(searchText)))
      .convertTo(LIST_OF_MEDICAMENTOS_TO);
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

    return createOperation()
      .insideASession()
      .taking(newEntity)
      .applyingResultOf(Save::create)
      .convertTo(MedicamentoTo.class);
  }

  @GET
  @Path("/{entityId}")
  public MedicamentoTo getSingleEntity(@PathParam("entityId") Long medicamentoId) {
    return createOperation()
      .insideASession()
      .applying(FindById.create(Medicamento.class, medicamentoId))
      .mapping((encontrado) -> encontrado.orElseThrowRuntime(() -> new WebApplicationException("medicamento not found", 404)))
      .convertTo(MedicamentoTo.class);
  }


  @PUT
  @Path("/{entityId}")
  public MedicamentoTo editEntity(MedicamentoTo newState, @PathParam("entityId") Long medicamentoId) {
    return createOperation()
      .insideATransaction()
      .taking(newState)
      .convertingTo(Medicamento.class)
      .mapping((encontrado) -> {
        if (encontrado == null) {
          throw new WebApplicationException("Medicamento not found", 404);
        }
        return encontrado;
      })
      .applyingResultOf(Save::create)
      .convertTo(MedicamentoTo.class);
  }

  @DELETE
  @Path("/{procedureId}")
  public void deleteProcedure(@PathParam("procedureId") Long medicamentoId) {
    createOperation()
      .insideATransaction()
      .apply(DeleteById.create(Medicamento.class, medicamentoId));
  }

  public static MedicamentoResource create(DependencyInjector appInjector) {
    MedicamentoResource resource = new MedicamentoResource();
    resource.appInjector = appInjector;
    return resource;
  }

  private ApplicationOperation createOperation() {
    return ApplicationOperation.createFor(appInjector);
  }

}
