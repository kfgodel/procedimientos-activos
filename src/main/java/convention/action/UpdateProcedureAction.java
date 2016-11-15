package convention.action;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import convention.persistent.Procedure;
import convention.rest.api.tos.ProcedureTo;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import java.util.function.Function;

/**
 * This type represents de frontend action that can be used to update a procedure info
 * Created by kfgodel on 14/11/16.
 */
@Resource(name = "PUT/procedure")
public class UpdateProcedureAction implements Function<ProcedureTo, ProcedureTo> {

  @Override
  public ProcedureTo apply(ProcedureTo newState) {
    return createOperation()
      .insideATransaction()
      .taking(newState)
      .convertingTo(Procedure.class)
      .applyingResultOf((editedProcedure) -> {
        // Answer 404 instead of null if it doesn't exist
        if (editedProcedure == null) {
          throw new WebApplicationException("procedure not found", 404);
        }
        return Save.create(editedProcedure);
      })
      .convertTo(ProcedureTo.class);
  }

  @Inject
  private DependencyInjector injector;

  private ApplicationOperation createOperation() {
    return ApplicationOperation.createFor(injector);
  }

}
