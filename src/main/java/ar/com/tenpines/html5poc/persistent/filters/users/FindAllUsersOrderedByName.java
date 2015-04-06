package ar.com.tenpines.html5poc.persistent.filters.users;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.impl.NaryFromNative;
import ar.com.tenpines.orm.api.operations.CrudOperation;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import convention.persistent.QUsuario;
import convention.persistent.Usuario;
import org.hibernate.Session;

import java.util.List;

/**
 * This type represents the filter that searches for all users and list them ordered by name
 *
 * Created by kfgodel on 04/04/15.
 */
public class FindAllUsersOrderedByName implements CrudOperation<Usuario> {

    @Override
    public Nary<Usuario> applyUsing(Session session) {
        QUsuario usuario = QUsuario.usuario;

        HibernateQuery query = new HibernateQuery(session);
        List<Usuario> foundUsuarios = query.from(usuario)
                .orderBy(usuario.name.asc())
                .list(usuario);
        return NaryFromNative.create(foundUsuarios.stream());
        
    }

    public static FindAllUsersOrderedByName create() {
        FindAllUsersOrderedByName filter = new FindAllUsersOrderedByName();
        return filter;
    }

}
