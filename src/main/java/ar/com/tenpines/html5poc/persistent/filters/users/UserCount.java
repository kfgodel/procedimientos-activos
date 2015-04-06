package ar.com.tenpines.html5poc.persistent.filters.users;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.impl.NaryFromNative;
import ar.com.tenpines.orm.api.operations.CrudOperation;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import convention.persistent.QUsuario;
import org.hibernate.Session;

/**
 * This type represents filter that counts the number of users in the database
 *
 * Created by kfgodel on 04/04/15.
 */
public class UserCount implements CrudOperation<Long> {

    @Override
    public Nary<Long> applyUsing(Session session) {
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
