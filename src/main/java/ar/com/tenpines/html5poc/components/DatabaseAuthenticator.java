package ar.com.tenpines.html5poc.components;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.webbyconvention.auth.api.WebCredential;
import convention.persistent.Usuario;
import ar.com.tenpines.html5poc.persistent.filters.users.UserByCredentials;
import ar.com.tenpines.html5poc.persistent.filters.users.UserCount;
import ar.com.tenpines.orm.api.HibernateOrm;

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
        Nary<Usuario> foundUser = hibernate.ensureSessionFor((context) -> {
            // If there are no users allow anyone to authenticate
            Nary<Long> userCount = UserCount.create().applyWithSessionOn(context);
            if (userCount.get() < 1) {
                return Nary.of(getProtoUser());
            }
            // If there are users, try to get the one for the credentials
            return UserByCredentials.create(credentials).applyWithSessionOn(context);
        });

        // Use the id as the web identification
        Optional<Object> aLong = foundUser.asNativeOptional().map(Usuario::getId);
        return aLong;
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
