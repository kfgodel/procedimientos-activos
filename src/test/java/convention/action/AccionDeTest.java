package convention.action;

import javax.annotation.Resource;
import java.util.function.Function;

/**
 * Created by kfgodel on 12/11/16.
 */
@Resource(name = "GET/test")
public class AccionDeTest implements Function<Object, Object> {

  @Override
  public Object apply(Object o) {
    return null;
  }
}
