package ar.com.tenpines.html5poc.persistent.filters.users;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.impl.NaryFromNative;
import ar.com.kfgodel.webbyconvention.auth.api.WebCredential;
import ar.com.tenpines.html5poc.persistent.QUsuario;
import ar.com.tenpines.html5poc.persistent.Usuario;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import org.hibernate.Session;

import java.util.function.Function;

/**
 * This type represents a filter that looks for a single user with given login and password
 * Created by kfgodel on 04/04/15.
 */
public class UserByCredentials implements Function<Session, Nary<Usuario>> {

    private WebCredential credentials;

    @Override
    public Nary<Usuario> apply(Session session) {
        QUsuario usuario = QUsuario.usuario;

        Usuario foundUsuario = new HibernateQuery(session).from(usuario)
                .where(usuario.login.eq(credentials.getUsername())
                        .and(usuario.password.eq(credentials.getPassword())))
                .uniqueResult(usuario);
        return NaryFromNative.ofNullable(foundUsuario);
    }

    public static UserByCredentials create(WebCredential credentials) {
        UserByCredentials filter = new UserByCredentials();
        filter.credentials = credentials;
        return filter;
    }

}
