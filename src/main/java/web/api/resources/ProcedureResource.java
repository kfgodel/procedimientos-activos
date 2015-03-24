package web.api.resources;

import ar.com.tenpines.html5poc.Application;
import ar.com.tenpines.html5poc.persistent.Procedure;
import web.api.resources.tos.ProcedureTo;

import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This type represents the resource to access procedures
 * Created by kfgodel on 17/03/15.
 */
public class ProcedureResource {

    private Application application;

    @GET
    public List<ProcedureTo> getAllProceduresUsers(){
        List<Procedure> procedimientos = application.getHibernate().doWithSession((context) ->
            context.getSession().createCriteria(Procedure.class).list()
        );

        List<ProcedureTo> proceduresTo = new ArrayList<>(procedimientos.size());
        for (Procedure procedure : procedimientos) {
            ProcedureTo userTo = createTo(procedure);
            proceduresTo.add(userTo);
        }

        return proceduresTo;
    }

    private ProcedureTo createTo(Procedure procedure) {
        return ProcedureTo.create(procedure.getId(), procedure.getName(), procedure.getDescription());
    }

    @POST
    public ProcedureTo createUser(){
        Procedure newProcedure = Procedure.create("Procedimiento nn", "Sin descripción");

        application.getHibernate().doInTransaction((context) -> context.getSession().save(newProcedure));

        return createTo(newProcedure);
    }

    @GET
    @Path("/{procedureId}")
    public ProcedureTo getSingleProcedure(@PathParam("procedureId") Long procedureId){
        Procedure procedure = application.getHibernate().doWithSession(context -> (Procedure) context.getSession().get(Procedure.class, procedureId));
        if(procedure != null){
            return createTo(procedure);
        }
        throw new WebApplicationException("procedure not found", 404);
    }


    @PUT
    @Path("/{procedureId}")
    public ProcedureTo editUser(ProcedureTo newState, @PathParam("procedureId") Long procedureId){
        Procedure procedure = application.getHibernate().doInTransaction(context -> {
            Procedure editedProcedure = (Procedure) context.getSession().get(Procedure.class, procedureId);
            if (editedProcedure == null) {
                throw new WebApplicationException("procedure not found", 404);
            }
            updateFromTo(newState, editedProcedure);
            context.getSession().saveOrUpdate(editedProcedure);
            return editedProcedure;
        });

        return createTo(procedure);
    }

    private void updateFromTo(ProcedureTo newState, Procedure editedProcedure) {
        editedProcedure.setDescription(newState.getDescription());
        editedProcedure.setName(newState.getName());
    }

    @DELETE
    @Path("/{procedureId}")
    public ProcedureTo deleteUser(@PathParam("procedureId") Long procedureId){
        Procedure deletedProcedure = application.getHibernate().doInTransaction((context)-> {
            Procedure usuario = (Procedure) context.getSession().get(Procedure.class, procedureId);
            context.getSession().delete(usuario);
            return usuario;
        });

        return createTo(deletedProcedure);
    }

    public static ProcedureResource create(Application application) {
        ProcedureResource resource = new ProcedureResource();
        resource.application = application;
        return resource;
    }

}
