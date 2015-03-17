package web.api.resources;

import com.google.common.collect.Lists;
import web.api.resources.tos.EmberResponse;
import web.api.resources.tos.ProcedureTo;

import javax.ws.rs.GET;
import java.util.List;

/**
 * This type represents the resource to access procedures
 * Created by kfgodel on 17/03/15.
 */
public class ProcedureResource {

    private List<ProcedureTo> procedures = Lists.newArrayList(
            ProcedureTo.create(1L, "Proceso 1", "Descripcion 1"),
            ProcedureTo.create(2L, "Proceso 2", "Descripcion 2"),
            ProcedureTo.create(3L,"Proceso 3", "Descripcion 3"),
            ProcedureTo.create(4L,"Proceso 4", "Descripcion 4")
    );

    @GET
    public EmberResponse getAllProceduresUsers(){
        return EmberResponse.create("procedures", procedures);
    }
}
