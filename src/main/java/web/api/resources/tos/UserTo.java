package web.api.resources.tos;

/**
 * This type represents a user for the frontend
 * Created by kfgodel on 03/03/15.
 */
public class UserTo {
    
    private Long id;
    private String name;
    private String login;
    private String password;
    private String creation;
    private String modification;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        userTo.id = id;
        userTo.name = name;
        userTo.login = login;
        userTo.password = password;
        return userTo;
    }

}
