package ar.com.tenpines.html5poc.components;

import ar.com.kfgodel.webbyconvention.auth.api.WebCredential;
import ar.com.tenpines.html5poc.persistent.Usuario;
import ar.com.tenpines.orm.api.HibernateOrm;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.Optional;
import java.util.function.Function;

/**
 * This type represents the web authenticator that uses the database to get check for correct credentials
 * Created by kfgodel on 02/04/15.
 */
public class DatabaseAuthenticator implements Function<WebCredential, Optional<Object>> {

    private HibernateOrm hibernate;
    private Usuario noUser;


    @Override
    public Optional<Object> apply(WebCredential credentials) {
        Usuario foundUser = hibernate.doWithSession((context) -> {
            // If there are no users allow anyone to authenticate
            Criteria countCriteria = context.getSession().createCriteria(Usuario.class);
            countCriteria.setProjection(Projections.rowCount());
            Number userCount = (Number) countCriteria.uniqueResult();
            if (userCount.intValue() < 1) {
                return getProtoUser();
            }

            Criteria usuarioCriteria = context.getSession().createCriteria(Usuario.class);
            usuarioCriteria.add(Restrictions.eq(Usuario.login_FIELD, credentials.getUsername()));
            usuarioCriteria.add(Restrictions.eq(Usuario.password_FIELD, credentials.getPassword()));
            return (Usuario) usuarioCriteria.uniqueResult();
        });
        if(foundUser == null){
            return Optional.empty();
        }
        // Use the id as the web identification
        return Optional.of(foundUser.getId());
    }

    public static DatabaseAuthenticator create(HibernateOrm hibernate) {
        DatabaseAuthenticator authenticator = new DatabaseAuthenticator();
        authenticator.hibernate = hibernate;
        return authenticator;
    }

    /**
     * Returns the user used to create other user when the application has none
     * @return The fake user
     */
    public Usuario getProtoUser(){
        if (noUser == null) {
            noUser = Usuario.create("Proto user","","");
            noUser.setId(-1L);
        }
        return noUser;
    }
}
