package ar.com.kfgodel.actions;

import java.util.Map;
import java.util.function.Function;

/**
 * This type represents a frontend actionable code that takes a message as input, and
 * generates a message as response
 * Created by kfgodel on 12/11/16.
 */
public interface FrontendAction extends Function<Map<String, Object>, Object> {

  /**
   * El nombre de recurso que identifica a esta acccion para el frontend
   */
  String getNombreDeRecurso();
}
