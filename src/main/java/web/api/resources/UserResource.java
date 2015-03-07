package web.api.resources;

import com.google.common.collect.Lists;
import web.api.resources.tos.EmberResponse;
import web.api.resources.tos.UserEditionTo;
import web.api.resources.tos.UserTo;

import javax.ws.rs.*;
import java.util.HashMap;
import java.util.List;

/**
 * This type is the resource API for users
 * Created by kfgodel on 03/03/15.
 */
public class UserResource {

    private static int nextId = 3;
    private static List<UserTo> users = Lists.newArrayList(
            UserTo.create(1L, "Pepito Gonzola", "pepe", "1234"),
            UserTo.create(2L, "AdminTerminator", "admin", "1234"));

    @GET
    public EmberResponse getAllUsers(){
        return EmberResponse.create("users", users);
    }

    @POST
    public EmberResponse createUser(){
        UserTo newUser = UserTo.create((long) nextId++, "", "", "");
        users.add(newUser);
        return EmberResponse.create("user", newUser);
    }

    @GET
    @Path("/{userId}")
    public EmberResponse getSingleUser(@PathParam("userId") Long userId){
        for (int i = 0; i < users.size(); i++) {
            UserTo user = users.get(i);
            if(user.getId().equals(userId)){
                return EmberResponse.create("user", user);
            }
        }
        throw new WebApplicationException("user not found", 404);
    }


    @PUT
    @Path("/{userId}")
    public EmberResponse editUser(UserEditionTo edition, @PathParam("userId") Long userId){
        UserTo newUserState = edition.getUser();

        for (int i = 0; i < users.size(); i++) {
            UserTo user = users.get(i);
            if(user.getId().equals(userId)){
                newUserState.setId(userId);
                users.set(i, newUserState);
                break;
            }
        }

        return EmberResponse.create("user", newUserState);
    }

    @DELETE
    @Path("/{userId}")
    public EmberResponse deleteUser(@PathParam("userId") Long userId){
        for (int i = 0; i < users.size(); i++) {
            UserTo user = users.get(i);
            if(user.getId().equals(userId)){
                users.remove(i);
                break;
            }
        }
        return EmberResponse.create("user", new HashMap<>());
    }
}
