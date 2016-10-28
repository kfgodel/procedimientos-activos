package convention.action;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.proact.persistent.filters.procedures.ProceduresByTextPortionOrdByName;
import convention.persistent.Procedure;
import convention.rest.api.tos.ProcedureFilterTo;
import convention.rest.api.tos.ProcedureTo;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;

/**
 * This type represents the search procedures action that can be used from the frontend
 * Created by kfgodel on 27/10/16.
 */
public class FindProceduresAction implements Function<ProcedureFilterTo, List<Procedure>> {

  private static final Type LIST_OF_PROCEDURES_TO = new ReferenceOf<List<ProcedureTo>>() {
  }.getReferencedType();

  @Inject
  private DependencyInjector injector;

  @Override
  public List<Procedure> apply(ProcedureFilterTo filter) {
    String searchText = filter.getSearchText();
    List<Procedure> operationResult = createOperation()
      .insideASession()
      .applying(ProceduresByTextPortionOrdByName.create(Nary.ofNullable(searchText)))
      .convertTo(LIST_OF_PROCEDURES_TO);
    return operationResult;
  }

  private ApplicationOperation createOperation() {
    return ApplicationOperation.createFor(injector);
  }

}
