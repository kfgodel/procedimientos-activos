package convention.rest.api.tos;

import ar.com.kfgodel.transformbyconvention.api.tos.PersistibleToSupport;
import convention.persistent.Medicamento;
import net.sf.kfgodel.bean2bean.annotations.CopyFromAndTo;

/**
 * This type represents a single medicamento for frontend
 * Created by kfgodel on 17/03/15.
 */
public class MedicamentoTo extends PersistibleToSupport {

    @CopyFromAndTo(Medicamento.name_FIELD)
    private String name;

    @CopyFromAndTo(Medicamento.description_FIELD)
    private String description;

    @CopyFromAndTo(Medicamento.tags_FIELD)
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

    public static MedicamentoTo create(Long id, String name, String description) {
        MedicamentoTo procedure = new MedicamentoTo();
        procedure.setId(id);
        procedure.name = name;
        procedure.description = description;
        return procedure;
    }

}
