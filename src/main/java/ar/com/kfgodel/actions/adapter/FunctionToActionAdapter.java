package ar.com.kfgodel.actions.adapter;

import ar.com.kfgodel.actions.FrontendAction;

import java.util.Map;
import java.util.function.Function;

/**
 * This type tranforms the original json map input to a specific type needed for a typed action
 * Created by kfgodel on 12/11/16.
 */
public class FunctionToActionAdapter implements FrontendAction {

  private Function<Map<String, Object>, Object> inputAdapter;
  private Function typedFunction;
  private Function<Object, Object> outputAdapter;
  private String nombreDeRecurso;

  @Override
  public Object apply(Map<String, Object> jsonInput) {
    Object functionInput = inputAdapter.apply(jsonInput);
    Object functionOutput = typedFunction.apply(functionInput);
    Object jsonOutput = outputAdapter.apply(functionOutput);
    return jsonOutput;
  }

  public static FunctionToActionAdapter create(String nombreDeRecurso, Function<Map<String, Object>, Object> inputAdapter, Function typedFunction, Function<Object, Object> outputAdapter) {
    FunctionToActionAdapter action = new FunctionToActionAdapter();
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
