package ar.com.kfgodel.actions.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.function.Function;

/**
 * This type knows how to adapt an input message to the expected type
 * Created by kfgodel on 12/11/16.
 */
public class JsonInputAdapter implements Function<Map<String, Object>, Object> {

  private Class<?> expectedInputType;
  private ObjectMapper objectMapper;

  @Override
  public Object apply(Map<String, Object> jsonInput) {
    Object adaptedInput = objectMapper.convertValue(jsonInput, expectedInputType);
    return adaptedInput;
  }

  public static JsonInputAdapter create(Class<?> expectedInputType, ObjectMapper objectMapper) {
    JsonInputAdapter adapter = new JsonInputAdapter();
    adapter.objectMapper = objectMapper;
    adapter.expectedInputType = expectedInputType;
    return adapter;
  }

}
