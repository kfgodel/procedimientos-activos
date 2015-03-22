package ar.com.tenpines.html5poc.persistent;

import javax.persistence.Entity;

/**
 * This type represents a user in the database
 * Created by kfgodel on 22/03/15.
 */
@Entity
public class Usuario extends PersistibleSupport {

    private String name;
    public static final String name_FIELD = "name";

    private String login;
    public static final String login_FIELD = "login";

    private String password;
    public static final String password_FIELD = "password";
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
