package convention.action.users;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import convention.persistent.Usuario;
import convention.rest.api.tos.UserTo;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import java.util.function.Function;

/**
 * This type represents the frontend action for changing a user state
 * Created by kfgodel on 17/11/16.
 */
@Resource(name = "PUT/user")
public class UpdateUserAction implements Function<UserTo, UserTo> {

  @Override
  public UserTo apply(UserTo newState) {
    return createOperation()
      .insideATransaction()
      .taking(newState)
      .convertingTo(Usuario.class)
      .mapping((encontrado) -> {
        // Answer 404 if missing
        if (encontrado == null) {
          throw new WebApplicationException("user not found", 404);
        }
        return encontrado;
      }).applyingResultOf(Save::create)
      .convertTo(UserTo.class);
  }

  @Inject
  private DependencyInjector injector;

  private ApplicationOperation createOperation() {
    return ApplicationOperation.createFor(injector);
  }
}
