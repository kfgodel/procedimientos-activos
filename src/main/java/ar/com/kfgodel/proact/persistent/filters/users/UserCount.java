package ar.com.kfgodel.proact.persistent.filters.users;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.orm.api.SessionContext;
import ar.com.kfgodel.orm.api.operations.SessionOperation;
import com.querydsl.jpa.hibernate.HibernateQueryFactory;
import convention.persistent.QUsuario;

/**
 * This type represents filter that counts the number of users in the database
 *
 * Created by kfgodel on 04/04/15.
 */
public class UserCount implements SessionOperation<Nary<Long>> {

    public static UserCount create() {
        UserCount userCount = new UserCount();
        return userCount;
    }

    @Override
    public Nary<Long> applyWithSessionOn(SessionContext sessionContext) {
        QUsuario usuario = QUsuario.usuario;
        Long userCount = new HibernateQueryFactory(sessionContext.getSession())
          .select(usuario.count())
          .from(usuario)
          .fetchCount();
        return Nary.of(userCount);
    }
}
