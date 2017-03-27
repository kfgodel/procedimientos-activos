package convention.action.session;

import javax.annotation.Resource;
import java.util.Map;
import java.util.function.Function;

/**
 * This type represents the action to retrieve the current http session
 * Created by kfgodel on 27/03/17.
 */
@Resource(name = "GET/session")
public class GetSessionAction implements Function<Map<String, Object>, String> {
  @Override
  public String apply(Map<String, Object> stringObjectMap) {
    // Return something so the response doesn't go empty handed
    return "{ \"status\": \"engaged\"}";
  }
}
