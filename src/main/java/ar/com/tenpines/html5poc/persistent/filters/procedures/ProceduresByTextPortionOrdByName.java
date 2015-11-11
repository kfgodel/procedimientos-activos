package ar.com.tenpines.html5poc.persistent.filters.procedures;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.impl.NaryFromNative;
import ar.com.tenpines.orm.api.operations.CrudOperation;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import convention.persistent.Procedure;
import convention.persistent.QProcedure;
import org.hibernate.Session;

import java.util.List;

/**
 * This type represents a filter that fetches all the procedures ordered by name
 * Created by kfgodel on 04/04/15.
 */
public class ProceduresByTextPortionOrdByName implements CrudOperation<Procedure> {

    private String filterText;

    @Override
    public Nary<Procedure> applyUsing(Session session) {
        QProcedure procedure = QProcedure.procedure;
        HibernateQuery query = new HibernateQuery(session)
          .from(procedure)
          .where(procedure.name.contains(filterText).or(procedure.description.contains(filterText)));

        List<Procedure> foundProcedures = query
                .orderBy(procedure.name.asc())
                .list(procedure);
        return NaryFromNative.create(foundProcedures.stream());
    }

    public static ProceduresByTextPortionOrdByName create(String filterText) {
        ProceduresByTextPortionOrdByName filter = new ProceduresByTextPortionOrdByName();
        filter.filterText = filterText;
        return filter;
    }

}
