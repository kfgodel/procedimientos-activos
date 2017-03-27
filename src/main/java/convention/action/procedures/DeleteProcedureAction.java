package convention.action.procedures;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.orm.api.operations.basic.DeleteById;
import convention.persistent.Procedure;
import convention.rest.api.tos.IdReferenceTo;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.function.Function;

/**
 * This type represents the frontend action to delete a procedure
 * Created by kfgodel on 14/11/16.
 */
@Resource(name = "DELETE/procedure")
public class DeleteProcedureAction implements Function<IdReferenceTo, Void> {
  @Override
  public Void apply(IdReferenceTo reference) {
    createOperation()
      .insideATransaction()
      .apply(DeleteById.create(Procedure.class, reference.getId()));
    return null;
  }

  @Inject
  private DependencyInjector injector;

  private ApplicationOperation createOperation() {
    return ApplicationOperation.createFor(injector);
  }

}
