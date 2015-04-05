package ar.com.tenpines.html5poc.persistent.filters.procedures;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.impl.NaryFromNative;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import convention.persistent.Procedure;
import convention.persistent.QProcedure;
import org.hibernate.Session;

import java.util.List;
import java.util.function.Function;

/**
 * This type represents a filter that fetches all the procedures ordered by name
 * Created by kfgodel on 04/04/15.
 */
public class AllProceduresOrdByName implements Function<Session, Nary<Procedure>> {
    @Override
    public Nary<Procedure> apply(Session session) {
        QProcedure procedure = QProcedure.procedure;
        List<Procedure> foundProcedures = new HibernateQuery(session)
                .from(procedure)
                .orderBy(procedure.name.asc())
                .list(procedure);
        return NaryFromNative.create(foundProcedures.stream());
    }

    public static AllProceduresOrdByName create() {
        AllProceduresOrdByName filter = new AllProceduresOrdByName();
        return filter;
    }

}
