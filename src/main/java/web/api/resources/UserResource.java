package web.api.resources;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.tenpines.html5poc.Application;
import ar.com.tenpines.html5poc.persistent.filters.users.FindAllUsersOrderedByName;
import ar.com.tenpines.orm.api.operations.basic.DeleteById;
import ar.com.tenpines.orm.api.operations.basic.FindById;
import ar.com.tenpines.orm.api.operations.basic.Save;
import convention.persistent.Usuario;
import web.api.resources.tos.UserTo;

import javax.ws.rs.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This type is the resource API for users
 * Created by kfgodel on 03/03/15.
 */
public class UserResource {

    private Application application;

    @GET
    public List<UserTo> getAllUsers(){
        Nary<Usuario> usuarios = application.getHibernate().doWithSession(FindAllUsersOrderedByName.create());

        List<UserTo> userTos = usuarios.map(this::createTo)
                .collect(Collectors.toList());

        return userTos;
    }

    private UserTo createTo(Usuario usuario) {
        UserTo userTo = application.getTransformer().transformTo(UserTo.class, usuario);
        return userTo;
    }

    @POST
    public UserTo createUser(){
        Usuario nuevoUsuario = Usuario.create("Sin nombre", "", "");

        application.getHibernate().doUnderTransaction(Save.create(nuevoUsuario));

        return createTo(nuevoUsuario);
    }

    @GET
    @Path("/{userId}")
    public UserTo getSingleUser(@PathParam("userId") Long userId){
        Nary<Usuario> usuario = application.getHibernate().doWithSession(FindById.create(Usuario.class, userId));
        return usuario.mapOptional(this::createTo)
                .orElseThrow(()->new WebApplicationException("user not found", 404));
    }


    @PUT
    @Path("/{userId}")
    public UserTo editUser(UserTo newUserState, @PathParam("userId") Long userId){

        Usuario usuario = application.getHibernate().doUnderTransaction(context -> {
            Usuario editedUsuario = this.application.getTransformer().transformTo(Usuario.class, newUserState);
            if (editedUsuario == null) {
                throw new WebApplicationException("user not found", 404);
            }
            context.save(editedUsuario);
            return editedUsuario;
        });


        return createTo(usuario);
    }

    @DELETE
    @Path("/{userId}")
    public UserTo deleteUser(@PathParam("userId") Long userId){
        application.getHibernate().doUnderTransaction(DeleteById.create(Usuario.class, userId));

        return UserTo.create(userId,"","","");
    }

    public static UserResource create(Application application) {
        UserResource resource = new UserResource();
        resource.application = application;
        return resource;
    }

}
