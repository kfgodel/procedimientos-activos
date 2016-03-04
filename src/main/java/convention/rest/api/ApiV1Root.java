package convention.rest.api;

import ar.com.tenpines.html5poc.Application;

import javax.inject.Inject;
import javax.ws.rs.Path;

/**
 * This type represents the root resource for v1 api access.<br>
 *     This allows multiple versions of the api in the same server (if needed)
 *     *
 * Created by kfgodel on 03/03/15.
 */
@Path("/api/v1")
public class ApiV1Root {

    @Inject
    private Application application;

    private UserResource users;
    private ProcedureResource procedures;
    private SessionResource session;

    @Path("/session")
    public SessionResource session(){
        if(session == null){
            session = SessionResource.create();
        }
        return session;
    }

    @Path("/users")
    public UserResource users(){
        if (users == null) {
            users = UserResource.create(application);
        }
        return users;
    }

    @Path("/procedures")
    public ProcedureResource procedures(){
        if (procedures == null) {
            procedures = ProcedureResource.create(application);
        }
        return procedures;
    }
}
