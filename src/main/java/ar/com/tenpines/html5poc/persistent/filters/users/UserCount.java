package ar.com.tenpines.html5poc.persistent.filters.users;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.impl.NaryFromNative;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import convention.persistent.QUsuario;
import org.hibernate.Session;

import java.util.function.Function;

/**
 * This type represents filter that counts the number of users in the database
 *
 * Created by kfgodel on 04/04/15.
 */
public class UserCount implements Function<Session, Nary<Long>> {

    @Override
    public Nary<Long> apply(Session session) {
        QUsuario usuario = QUsuario.usuario;
        Long userCount = new HibernateQuery(session)
                .from(usuario)
                .uniqueResult(usuario.count());
        return NaryFromNative.of(userCount);
    }

    public static UserCount create() {
        UserCount userCount = new UserCount();
        return userCount;
    }

}
