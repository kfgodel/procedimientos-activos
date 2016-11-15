package convention.action;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import convention.persistent.Procedure;
import convention.rest.api.tos.ProcedureTo;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/**
 * This type knows how to create a procedure from scratch for the frontend
 * Created by kfgodel on 14/11/16.
 */
@Resource(name = "POST/procedure")
public class CreateProcedure implements Function<Map<String, Object>, ProcedureTo> {

  @Override
  public ProcedureTo apply(Map<String, Object> stringObjectMap) {
    return createOperation()
      .insideASession()
      .taking(Procedure.create("Procedimiento " + UUID.randomUUID(), "Sin descripción"))
      .applyingResultOf(Save::create)
      .convertTo(ProcedureTo.class);
  }

  @Inject
  private DependencyInjector injector;

  private ApplicationOperation createOperation() {
    return ApplicationOperation.createFor(injector);
  }

}
