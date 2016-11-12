package convention.action;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.orm.api.operations.basic.FindById;
import convention.persistent.Procedure;
import convention.rest.api.tos.IdReferenceTo;
import convention.rest.api.tos.ProcedureTo;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import java.util.function.Function;

/**
 * This type represents the find single procedure action fron the frontend
 * Created by kfgodel on 12/11/16.
 */
@Resource(name = "GET/procedure")
public class FindProcedureAction implements Function<IdReferenceTo, ProcedureTo> {

  @Override
  public ProcedureTo apply(IdReferenceTo idReferenceTo) {
    return createOperation()
      .insideASession()
      .applying(FindById.create(Procedure.class, idReferenceTo.getId()))
      .mapping((foundProcedure) -> {
        // Answer 404 instead of null if it doesn't exist
        return foundProcedure.orElseThrowRuntime(() -> new WebApplicationException("procedure not found", 404));
      })
      .convertTo(ProcedureTo.class);
  }

  @Inject
  private DependencyInjector injector;

  private ApplicationOperation createOperation() {
    return ApplicationOperation.createFor(injector);
  }


}
