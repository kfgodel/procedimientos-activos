package ar.com.tenpines.html5poc.persistent.filters;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.impl.NaryFromNative;
import ar.com.tenpines.html5poc.persistent.QUsuario;
import ar.com.tenpines.html5poc.persistent.Usuario;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import org.hibernate.Session;

import java.util.List;
import java.util.function.Function;

/**
 * This type represents the filter that searches for all users and list them ordered by name
 *
 * Created by kfgodel on 04/04/15.
 */
public class AllUsersOrderedByName implements Function<Session, Nary<Usuario>> {

    @Override
    public Nary<Usuario> apply(Session session) {
        QUsuario usuario = QUsuario.usuario;

        HibernateQuery query = new HibernateQuery(session);
        List<Usuario> foundUsuarios = query.from(usuario)
                .orderBy(usuario.name.asc())
                .list(usuario);
        return NaryFromNative.create(foundUsuarios.stream());
        
    }

    public static AllUsersOrderedByName create() {
        AllUsersOrderedByName filter = new AllUsersOrderedByName();
        return filter;
    }

}
