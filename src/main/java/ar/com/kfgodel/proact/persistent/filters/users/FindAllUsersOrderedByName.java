package ar.com.kfgodel.proact.persistent.filters.users;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.orm.api.SessionContext;
import ar.com.kfgodel.orm.api.operations.SessionOperation;
import com.querydsl.jpa.hibernate.HibernateQueryFactory;
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

        HibernateQueryFactory queryFactory = new HibernateQueryFactory(sessionContext.getSession());
        List<Usuario> foundUsuarios = queryFactory.selectFrom(usuario)
          .orderBy(usuario.name.asc())
          .fetch();
        return Nary.create(foundUsuarios.stream());
    }
}
