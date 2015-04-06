package web.api.resources.tos;

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
        procedure.setId(id);
        procedure.name = name;
        procedure.description = description;
        return procedure;
    }

}
