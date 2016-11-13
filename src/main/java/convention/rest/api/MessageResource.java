package convention.rest.api;

import ar.com.kfgodel.actions.BuscadorDeAccionesEnClasspath;
import ar.com.kfgodel.actions.FrontendAction;
import ar.com.kfgodel.actions.adapter.AdaptadorDeFuncionEnAccion;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This type represents the resource to access procedures
 * Created by kfgodel on 17/03/15.
 */
@Produces("application/json")
@Consumes("application/json")
public class MessageResource {

  public static final String RECURSO_KEY = "recurso";

  @Inject
  private DependencyInjector appInjector;

  @Inject
  private ObjectMapper objectMapper;

  private Map<String, FrontendAction> accionesPorRecurso;


  @POST
  public Object processMessage(Map<String, Object> messageContent) {
    FrontendAction accion = buscarAccionPara(messageContent)
      .orElseThrow(() -> new WebApplicationException("Accion no encontrada para el mensaje", 404));
    Object response = accion.apply(messageContent);
    return response;
  }

  private Optional<FrontendAction> buscarAccionPara(Map<String, Object> messageContent) {
    Object nombreDeRecurso = messageContent.get(RECURSO_KEY);
    FrontendAction frontendAction = getAccionesPorRecurso().get(nombreDeRecurso);
    return Optional.ofNullable(frontendAction);
  }

  public Map<String, FrontendAction> getAccionesPorRecurso() {
    if (accionesPorRecurso == null) {
      accionesPorRecurso = inicializarAccionesPorRecurso();
    }
    return accionesPorRecurso;
  }

  private Map<String, FrontendAction> inicializarAccionesPorRecurso() {
    Map<String, FrontendAction> accionesPorRecurso = new HashMap<>();
    BuscadorDeAccionesEnClasspath buscador = BuscadorDeAccionesEnClasspath.create(AdaptadorDeFuncionEnAccion.create(appInjector, objectMapper));
    List<FrontendAction> acciones = buscador.buscarAccionesDisponibles();
    acciones.forEach((accion) -> {
      accionesPorRecurso.put(accion.getNombreDeRecurso(), accion);
    });
    return accionesPorRecurso;
  }

}
