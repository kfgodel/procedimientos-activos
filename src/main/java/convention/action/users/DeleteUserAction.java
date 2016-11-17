package convention.action.users;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.orm.api.operations.basic.DeleteById;
import convention.persistent.Usuario;
import convention.rest.api.tos.IdReferenceTo;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.function.Function;

/**
 * This type represents the frontend action for removing a user
 * <p>
 * Created by kfgodel on 17/11/16.
 */
@Resource(name = "DELETE/user")
public class DeleteUserAction implements Function<IdReferenceTo, Void> {
  @Override
  public Void apply(IdReferenceTo reference) {
    createOperation()
      .insideATransaction()
      .apply(DeleteById.create(Usuario.class, reference.getId()));
    return null;
  }

  @Inject
  private DependencyInjector injector;

  private ApplicationOperation createOperation() {
    return ApplicationOperation.createFor(injector);
  }

}
