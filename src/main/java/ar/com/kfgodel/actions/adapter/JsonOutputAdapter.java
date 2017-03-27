package ar.com.kfgodel.actions.adapter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * This type knows how to convert a typed output into a json message output
 * Created by kfgodel on 12/11/16.
 */
public class JsonOutputAdapter implements Function<Object, Object> {

  private ObjectMapper objectMapper;

  private MapType jsonHashType;
  private CollectionType jsonListType;

  @Override
  public Object apply(Object originalOutput) {
    JavaType expectedType = calculateExpectedTypeFor(originalOutput);
    Object jsonOutput = objectMapper.convertValue(originalOutput, expectedType);
    return jsonOutput;
  }

  private JavaType calculateExpectedTypeFor(Object originalOutput) {
    if (originalOutput instanceof Collection) {
      return getJsonArrayType();
    }else if(originalOutput instanceof String){
      return getTypeFactory().constructType(String.class);
    }
    return getJsonHashType();
  }

  private MapType getJsonHashType() {
    if (jsonHashType == null) {
      jsonHashType = getTypeFactory().constructMapType(Map.class, String.class, Object.class);
    }
    return jsonHashType;
  }

  private CollectionType getJsonArrayType() {
    if (jsonListType == null) {
      jsonListType = getTypeFactory().constructCollectionType(List.class, Object.class);
    }
    return jsonListType;
  }

  private TypeFactory getTypeFactory() {
    return objectMapper.getTypeFactory();
  }

  public static JsonOutputAdapter create(ObjectMapper objectMapper) {
    JsonOutputAdapter adapter = new JsonOutputAdapter();
    adapter.objectMapper = objectMapper;
    return adapter;
  }

}
