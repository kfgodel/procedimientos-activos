package convention.action.users;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.orm.api.operations.basic.FindById;
import convention.persistent.Usuario;
import convention.rest.api.tos.IdReferenceTo;
import convention.rest.api.tos.UserTo;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import java.util.function.Function;

/**
 * This type represents the action for retrieving a single user
 * Created by kfgodel on 17/11/16.
 */
@Resource(name = "GET/user")
public class FindUserAction implements Function<IdReferenceTo, UserTo> {

  @Override
  public UserTo apply(IdReferenceTo idReferenceTo) {
    return createOperation()
      .insideASession()
      .applying(FindById.create(Usuario.class, idReferenceTo.getId()))
      .mapping((encontrado) -> {
        // Answer 404 if missing
        return encontrado.orElseThrowRuntime(() -> new WebApplicationException("user not found", 404));
      })
      .convertTo(UserTo.class);
  }

  @Inject
  private DependencyInjector injector;

  private ApplicationOperation createOperation() {
    return ApplicationOperation.createFor(injector);
  }


}
