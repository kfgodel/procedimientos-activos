package web.api.resources;

import com.google.common.collect.Lists;
import web.api.resources.tos.*;

import javax.ws.rs.*;
import java.util.HashMap;
import java.util.List;

/**
 * This type represents the resource to access procedures
 * Created by kfgodel on 17/03/15.
 */
public class ProcedureResource {


    private int nextId = 5;

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

    @POST
    public EmberResponse createUser(){
        ProcedureTo procedure = ProcedureTo.create((long) nextId++, "Procedimiento " + nextId, "Sin descripcion");
        procedures.add(procedure);
        return EmberResponse.create("procedure", procedure);
    }

    @GET
    @Path("/{procedureId}")
    public EmberResponse getSingleProcedure(@PathParam("procedureId") Long procedureId){
        for (int i = 0; i < procedures.size(); i++) {
            ProcedureTo user = procedures.get(i);
            if(user.getId().equals(procedureId)){
                return EmberResponse.create("procedure", user);
            }
        }
        throw new WebApplicationException("procedure not found", 404);
    }


    @PUT
    @Path("/{procedureId}")
    public EmberResponse editUser(ProcedureEditionTo edition, @PathParam("procedureId") Long procedureId){
        ProcedureTo editedProcedure = edition.getProcedure();

        for (int i = 0; i < procedures.size(); i++) {
            ProcedureTo user = procedures.get(i);
            if(user.getId().equals(procedureId)){
                editedProcedure.setId(procedureId);
                procedures.set(i, editedProcedure);
                break;
            }
        }

        return EmberResponse.create("procedure", editedProcedure);
    }

    @DELETE
    @Path("/{procedureId}")
    public EmberResponse deleteUser(@PathParam("procedureId") Long procedureId){
        for (int i = 0; i < procedures.size(); i++) {
            ProcedureTo procedure = procedures.get(i);
            if(procedure.getId().equals(procedureId)){
                procedures.remove(i);
                break;
            }
        }
        return EmberResponse.create("procedure", new HashMap<>());
    }

}
