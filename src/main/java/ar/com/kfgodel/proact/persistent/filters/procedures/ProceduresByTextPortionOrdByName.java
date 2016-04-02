package ar.com.kfgodel.proact.persistent.filters.procedures;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.api.optionals.Optional;
import ar.com.kfgodel.orm.api.SessionContext;
import ar.com.kfgodel.orm.api.operations.SessionOperation;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import convention.persistent.Procedure;
import convention.persistent.QProcedure;

import java.util.List;

/**
 * This type represents a filter that fetches all the procedures ordered by name
 * Created by kfgodel on 04/04/15.
 */
public class ProceduresByTextPortionOrdByName implements SessionOperation<Nary<Procedure>> {

  private Optional<String> filterText;

  public static ProceduresByTextPortionOrdByName create(Optional<String> filterText) {
    ProceduresByTextPortionOrdByName filter = new ProceduresByTextPortionOrdByName();
    filter.filterText = filterText;
    return filter;
  }

  @Override
  public Nary<Procedure> applyWithSessionOn(SessionContext sessionContext) {
    QProcedure procedure = QProcedure.procedure;
    HibernateQuery query = new HibernateQuery(sessionContext.getSession())
      .from(procedure);

    filterText.ifPresent((textToRestrict)->{
      query.where(
        procedure.name.containsIgnoreCase(textToRestrict)
          .or(procedure.description.containsIgnoreCase(textToRestrict))
          .or(procedure.tags.containsIgnoreCase(textToRestrict))
      );
    });

    List<Procedure> foundProcedures = query
      .orderBy(procedure.name.asc())
      .list(procedure);
    return Nary.create(foundProcedures);
  }
}
