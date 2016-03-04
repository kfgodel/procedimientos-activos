package convention.rest.api.tos;

import com.tenpines.commons.tos.PersistibleToSupport;
import convention.persistent.Procedure;
import net.sf.kfgodel.bean2bean.annotations.CopyFromAndTo;

/**
 * This type represents a sinlge procedure for frontend
 * Created by kfgodel on 17/03/15.
 */
public class ProcedureTo extends PersistibleToSupport {

    @CopyFromAndTo(Procedure.name_FIELD)
    private String name;

    @CopyFromAndTo(Procedure.description_FIELD)
    private String description;

    @CopyFromAndTo(Procedure.tags_FIELD)
    private String tags;

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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public static ProcedureTo create(Long id, String name, String description) {
        ProcedureTo procedure = new ProcedureTo();
        procedure.setId(id);
        procedure.name = name;
        procedure.description = description;
        return procedure;
    }

}
