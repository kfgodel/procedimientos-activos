package web.api.resources;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.tenpines.html5poc.Application;
import ar.com.tenpines.orm.api.operations.basic.Save;
import convention.persistent.Procedure;
import ar.com.tenpines.html5poc.persistent.filters.procedures.FindAllProceduresOrdByName;
import ar.com.tenpines.orm.api.operations.basic.DeleteById;
import ar.com.tenpines.orm.api.operations.basic.FindById;
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
        Nary<Procedure> procedimientos = application.getHibernate().doWithSession(FindAllProceduresOrdByName.create());

        return procedimientos
                .map(this::createTo)
                .collect(Collectors.toList());
    }

    private ProcedureTo createTo(Procedure procedure) {
        return this.application.getTransformer().transformTo(ProcedureTo.class, procedure);
    }

    @POST
    public ProcedureTo createUser(){
        Procedure newProcedure = Procedure.create("Procedimiento nn", "Sin descripción");

        application.getHibernate().doUnderTransaction(Save.create(newProcedure));

        return createTo(newProcedure);
    }

    @GET
    @Path("/{procedureId}")
    public ProcedureTo getSingleProcedure(@PathParam("procedureId") Long procedureId){
        Nary<Procedure> procedure = application.getHibernate().doWithSession(FindById.create(Procedure.class, procedureId));
        return procedure
                .mapOptional(this::createTo)
                .orElseThrow(() -> new WebApplicationException("procedure not found", 404));
    }


    @PUT
    @Path("/{procedureId}")
    public ProcedureTo editUser(ProcedureTo newState, @PathParam("procedureId") Long procedureId){
        Procedure procedure = application.getHibernate().doUnderTransaction(context -> {
            Procedure editedProcedure = this.application.getTransformer().transformTo(Procedure.class, newState);
            if (editedProcedure == null) {
                throw new WebApplicationException("procedure not found", 404);
            }
            context.save(editedProcedure);
            return editedProcedure;
        });

        return createTo(procedure);
    }

    @DELETE
    @Path("/{procedureId}")
    public ProcedureTo deleteUser(@PathParam("procedureId") Long procedureId){
        application.getHibernate().doUnderTransaction(DeleteById.create(Procedure.class, procedureId));

        return ProcedureTo.create(procedureId, "","");
    }

    public static ProcedureResource create(Application application) {
        ProcedureResource resource = new ProcedureResource();
        resource.application = application;
        return resource;
    }

}
