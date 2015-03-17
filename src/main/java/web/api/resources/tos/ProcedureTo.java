package web.api.resources.tos;

/**
 * This type represents a sinlge procedure for frontend
 * Created by kfgodel on 17/03/15.
 */
public class ProcedureTo {

    private Long id;
    private String name;
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static ProcedureTo create(Long id, String name, String description) {
        ProcedureTo procedure = new ProcedureTo();
        procedure.id = id;
        procedure.name = name;
        procedure.description = description;
        return procedure;
    }

}
