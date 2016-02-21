package ar.com.tenpines.html5poc.persistent.filters.users;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.impl.NaryFromNative;
import ar.com.kfgodel.webbyconvention.auth.api.WebCredential;
import ar.com.tenpines.orm.api.operations.CrudOperation;
import com.mysema.query.NonUniqueResultException;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import convention.persistent.QUsuario;
import convention.persistent.Usuario;
import org.hibernate.Session;

/**
 * This type represents a filter that looks for a single user with given login and password
 * Created by kfgodel on 04/04/15.
 */
public class UserByCredentials implements CrudOperation<Usuario> {

  private WebCredential credentials;

  @Override
  public Nary<Usuario> applyUsing(Session session) {
    QUsuario usuario = QUsuario.usuario;

    try {
      Usuario foundUsuario = new HibernateQuery(session)
        .from(usuario)
        .where(usuario.login.eq(credentials.getUsername())
          .and(usuario.password.eq(credentials.getPassword())))
        .uniqueResult(usuario);
      return NaryFromNative.ofNullable(foundUsuario);
    } catch (NonUniqueResultException e) {
      throw new IllegalStateException("There's more than one user with same credentials? " + credentials);
    }
  }

  public static UserByCredentials create(WebCredential credentials) {
    UserByCredentials filter = new UserByCredentials();
    filter.credentials = credentials;
    return filter;
  }

}
