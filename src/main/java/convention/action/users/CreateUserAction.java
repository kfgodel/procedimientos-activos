package convention.action.users;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import convention.persistent.Usuario;
import convention.rest.api.tos.UserTo;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.Map;
import java.util.function.Function;

/**
 * This type represents the frontend action for creating a new user
 * <p>
 * Created by kfgodel on 17/11/16.
 */
@Resource(name = "POST/user")
public class CreateUserAction implements Function<Map<String, Object>, UserTo> {

  @Override
  public UserTo apply(Map<String, Object> stringObjectMap) {
    return createOperation()
      .insideASession()
      .taking(Usuario.create("Sin nombre", "", ""))
      .applyingResultOf(Save::create)
      .convertTo(UserTo.class);
  }

  @Inject
  private DependencyInjector injector;

  private ApplicationOperation createOperation() {
    return ApplicationOperation.createFor(injector);
  }

}

