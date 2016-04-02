package convention.rest.api.tos;

import ar.com.kfgodel.appbyconvention.tos.PersistableToSupport;
import convention.persistent.Usuario;
import net.sf.kfgodel.bean2bean.annotations.CopyFrom;
import net.sf.kfgodel.bean2bean.annotations.CopyFromAndTo;

/**
 * This type represents a user for the frontend
 * Created by kfgodel on 03/03/15.
 */
public class UserTo extends PersistableToSupport {

    @CopyFromAndTo(Usuario.name_FIELD)
    private String name;

    @CopyFromAndTo(Usuario.login_FIELD)
    private String login;

    @CopyFromAndTo(Usuario.password_FIELD)
    private String password;

    @CopyFrom(Usuario.momentoDeCreacion_FIELD)
    private String creation;

    @CopyFrom(Usuario.momentoDeUltimaModificacion_FIELD)
    private String modification;

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

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }

    public String getModification() {
        return modification;
    }

    public void setModification(String modification) {
        this.modification = modification;
    }

    public static UserTo create(Long id, String name, String login, String password) {
        UserTo userTo = new UserTo();
        userTo.setId(id);
        userTo.name = name;
        userTo.login = login;
        userTo.password = password;
        return userTo;
    }

}
