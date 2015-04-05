package convention.persistent;

import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * This type represents a procedure to define how to do something
 * Created by kfgodel on 24/03/15.
 */
@Entity
public class Procedure extends PersistentSupport {

    private String name;
    public static final String name_FIELD = "name";

    @Lob
    private String description;
    public static final String description_FIELD = "description";


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Procedure create(String name, String description) {
        Procedure procedure = new Procedure();
        procedure.setName(name);
        procedure.setDescription(description);
        return procedure;
    }

}
