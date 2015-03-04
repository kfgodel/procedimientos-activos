package web.api.resources;

import com.google.common.collect.Lists;
import web.api.resources.tos.EmberResponse;
import web.api.resources.tos.UserTo;

import javax.ws.rs.GET;

/**
 * This type is the resource API for users
 * Created by kfgodel on 03/03/15.
 */
public class UserResource {
    
    @GET
    public EmberResponse getAllUsers(){
        return EmberResponse.create("users", Lists.newArrayList(
                UserTo.create(1L, "Pepito Gonzola", "pepe", "1234"),
                UserTo.create(2L, "AdminTerminator", "admin", "1234")));
    }
}
