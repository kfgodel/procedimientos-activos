package ar.com.kfgodel.actions.adapter;

import ar.com.kfgodel.actions.FrontendAction;

import java.util.Map;
import java.util.function.Function;

/**
 * This type tranforms the original json map input to a specific type needed for a typed action
 * Created by kfgodel on 12/11/16.
 */
public class FunctionAsActionAdapter implements FrontendAction {

  private Function<Map<String, Object>, Object> inputAdapter;
  private Function typedFunction;
  private Function<Object, Object> outputAdapter;
  private String nombreDeRecurso;

  @Override
  public Object apply(Map<String, Object> jsonInput) {
    Object adaptedInput = inputAdapter.apply(jsonInput);
    Object rawOutput = typedFunction.apply(adaptedInput);
    Object jsonOutput = outputAdapter.apply(rawOutput);
    return jsonOutput;
  }

  public static FunctionAsActionAdapter create(String nombreDeRecurso, Function<Map<String, Object>, Object> inputAdapter, Function typedFunction, Function<Object, Object> outputAdapter) {
    FunctionAsActionAdapter action = new FunctionAsActionAdapter();
    action.nombreDeRecurso = nombreDeRecurso;
    action.inputAdapter = inputAdapter;
    action.typedFunction = typedFunction;
    action.outputAdapter = outputAdapter;
    return action;
  }


  @Override
  public String getNombreDeRecurso() {
    return nombreDeRecurso;
  }
}
