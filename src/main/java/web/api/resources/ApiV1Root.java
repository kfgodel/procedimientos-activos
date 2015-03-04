package web.api.resources;

import javax.ws.rs.Path;

/**
 * This type represents the root resource for v1 api access.<br>
 *     This allows multiple versions of the api in the same server (if needed)
 *     *
 * Created by kfgodel on 03/03/15.
 */
@Path("/api/v1")
public class ApiV1Root {
    
    @Path("/users")
    public UserResource users(){
        return new UserResource();
    }
}
