package web.api.resources;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.tenpines.html5poc.Application;
import ar.com.tenpines.html5poc.persistent.Procedure;
import ar.com.tenpines.html5poc.persistent.filters.procedures.AllProceduresOrdByName;
import ar.com.tenpines.orm.api.operations.DeleteById;
import ar.com.tenpines.orm.api.operations.FindById;
import web.api.resources.tos.ProcedureTo;

import javax.ws.rs.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This type represents the resource to access procedures
 * Created by kfgodel on 17/03/15.
 */
public class ProcedureResource {

    private Application application;

    @GET
    public List<ProcedureTo> getAllProceduresUsers(){
        Nary<Procedure> procedimientos = application.getHibernate().doWithSession((context) ->
                        context.perform(AllProceduresOrdByName.create())
        );

        return procedimientos
                .map(this::createTo)
                .collect(Collectors.toList());
    }

    private ProcedureTo createTo(Procedure procedure) {
        return ProcedureTo.create(procedure.getId(), procedure.getName(), procedure.getDescription());
    }

    @POST
    public ProcedureTo createUser(){
        Procedure newProcedure = Procedure.create("Procedimiento nn", "Sin descripción");

        application.getHibernate().doInTransaction((context) -> context.save(newProcedure));

        return createTo(newProcedure);
    }

    @GET
    @Path("/{procedureId}")
    public ProcedureTo getSingleProcedure(@PathParam("procedureId") Long procedureId){
        Nary<Procedure> procedure = application.getHibernate().doWithSession(context -> context.perform(FindById.create(Procedure.class, procedureId)));
        return procedure
                .mapOptional(this::createTo)
                .orElseThrow(() -> new WebApplicationException("procedure not found", 404));
    }


    @PUT
    @Path("/{procedureId}")
    public ProcedureTo editUser(ProcedureTo newState, @PathParam("procedureId") Long procedureId){
        Procedure editedProcedure = application.getHibernate().doInTransaction(context -> {
            Nary<Procedure> found = context.perform(FindById.create(Procedure.class, procedureId));
            if (found.isAbsent()) {
                throw new WebApplicationException("procedure not found", 404);
            }
            Procedure procedure = found.get();
            updateFromTo(newState, procedure);
            context.save(procedure);
            return procedure;
        });

        return createTo(editedProcedure);
    }

    private void updateFromTo(ProcedureTo newState, Procedure editedProcedure) {
        editedProcedure.setDescription(newState.getDescription());
        editedProcedure.setName(newState.getName());
    }

    @DELETE
    @Path("/{procedureId}")
    public ProcedureTo deleteUser(@PathParam("procedureId") Long procedureId){
        application.getHibernate().doInTransaction((context)-> {
            context.perform(DeleteById.create(Procedure.class, procedureId));
            return null;
        });

        return ProcedureTo.create(procedureId, "","");
    }

    public static ProcedureResource create(Application application) {
        ProcedureResource resource = new ProcedureResource();
        resource.application = application;
        return resource;
    }

}
