package ar.com.tenpines.html5poc.persistent.filters.users;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.impl.NaryFromNative;
import ar.com.tenpines.orm.api.SessionContext;
import ar.com.tenpines.orm.api.operations.SessionOperation;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import convention.persistent.QUsuario;
import convention.persistent.Usuario;

import java.util.List;

/**
 * This type represents the filter that searches for all users and list them ordered by name
 *
 * Created by kfgodel on 04/04/15.
 */
public class FindAllUsersOrderedByName implements SessionOperation<Nary<Usuario>> {

    public static FindAllUsersOrderedByName create() {
        FindAllUsersOrderedByName filter = new FindAllUsersOrderedByName();
        return filter;
    }

    @Override
    public Nary<Usuario> applyWithSessionOn(SessionContext sessionContext) {
        QUsuario usuario = QUsuario.usuario;

        HibernateQuery query = new HibernateQuery(sessionContext.getSession());
        List<Usuario> foundUsuarios = query.from(usuario)
          .orderBy(usuario.name.asc())
          .list(usuario);
        return NaryFromNative.create(foundUsuarios.stream());
    }
}
