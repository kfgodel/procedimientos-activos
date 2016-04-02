package convention.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * This type represents a user in the database
 * Created by kfgodel on 22/03/15.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {Usuario.login_FIELD, Usuario.password_FIELD}))
public class Usuario extends PersistableSupport {

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

    public static Usuario create(String name, String login, String password) {
        Usuario usuario = new Usuario();
        usuario.setName(name);
        usuario.setLogin(login);
        usuario.setPassword(password);
        return usuario;
    }

}
