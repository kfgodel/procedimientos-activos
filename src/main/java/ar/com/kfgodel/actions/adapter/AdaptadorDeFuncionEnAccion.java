package ar.com.kfgodel.actions.adapter;

import ar.com.kfgodel.actions.FrontendAction;
import ar.com.kfgodel.actions.descriptor.DescriptorDeFuncionComoAccion;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.function.Function;

/**
 * This type knows how to convert a basic function into a frontend action
 * Created by kfgodel on 12/11/16.
 */
public class AdaptadorDeFuncionEnAccion {

  private DependencyInjector appInjector;
  private ObjectMapper objectMapper;
  private JsonOutputAdapter outputAdapter;

  public static AdaptadorDeFuncionEnAccion create(DependencyInjector appInjector, ObjectMapper objectMapper) {
    AdaptadorDeFuncionEnAccion adaptador = new AdaptadorDeFuncionEnAccion();
    adaptador.appInjector = appInjector;
    adaptador.objectMapper = objectMapper;
    return adaptador;
  }

  public FrontendAction adaptar(DescriptorDeFuncionComoAccion descriptor) {
    JsonInputAdapter inputAdapter = crearInputAdapter(descriptor.getExpectedInputType());

    Function typedFunction = appInjector.createInjected(descriptor.getFunctionType());

    JsonOutputAdapter outputAdapter = getOutputAdapter();

    String nombreDeRecurso = descriptor.getNombreDeRecurso();
    return FunctionToActionAdapter.create(nombreDeRecurso, inputAdapter, typedFunction, outputAdapter);
  }

  private JsonOutputAdapter getOutputAdapter() {
    if (outputAdapter == null) {
      outputAdapter = JsonOutputAdapter.create(objectMapper);
    }
    return outputAdapter;
  }

  private JsonInputAdapter crearInputAdapter(Class expectedInputType) {
    return JsonInputAdapter.create(expectedInputType, objectMapper);
  }
}
