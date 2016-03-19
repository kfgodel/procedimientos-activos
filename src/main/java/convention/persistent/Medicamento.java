package convention.persistent;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.Size;

/**
 * This type represents a medicamento description
 * Created by kfgodel on 24/03/15.
 */
@Entity
public class Medicamento extends PersistentSupport {

    @Size(max=1024)
    private String name;
    public static final String name_FIELD = "name";

    @Lob
    private String description;
    public static final String description_FIELD = "description";

    @Size(max=1024)
    private String tags;
    public static final String tags_FIELD = "tags";

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

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

    public static Medicamento create(String name, String description) {
        Medicamento procedure = new Medicamento();
        procedure.setName(name);
        procedure.setDescription(description);
        return procedure;
    }

}
