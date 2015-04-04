package web.api.resources;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.tenpines.html5poc.Application;
import ar.com.tenpines.html5poc.persistent.Usuario;
import ar.com.tenpines.orm.api.operations.DeleteById;
import ar.com.tenpines.orm.api.operations.FindById;
import ar.com.tenpines.orm.api.operations.GetAll;
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
        Nary<Usuario> usuarios = application.getHibernate()
                .doWithSession(context -> context.perform(GetAll.of(Usuario.class)));

        List<UserTo> userTos = usuarios.map(this::createTo)
                .collect(Collectors.toList());

        return userTos;
    }

    private UserTo createTo(Usuario usuario) {
        return UserTo.create(usuario.getId(), usuario.getName(), usuario.getLogin(), usuario.getPassword());
    }

    @POST
    public UserTo createUser(){
        Usuario nuevoUsuario = Usuario.create("Sin nombre", "", "");

        application.getHibernate().doInTransaction((context) -> context.save(nuevoUsuario));

        return createTo(nuevoUsuario);
    }

    @GET
    @Path("/{userId}")
    public UserTo getSingleUser(@PathParam("userId") Long userId){
        Nary<Usuario> usuario = application.getHibernate().doWithSession(context -> context.perform(FindById.create(Usuario.class, userId)));
        return usuario.mapOptional(this::createTo)
                .orElseThrow(()->new WebApplicationException("user not found", 404));
    }


    @PUT
    @Path("/{userId}")
    public UserTo editUser(UserTo newUserState, @PathParam("userId") Long userId){
        Usuario usuario = application.getHibernate().doInTransaction(context -> {
            Nary<Usuario> encontrado = context.perform(FindById.create(Usuario.class, userId));
            if (!encontrado.isPresent()) {
                throw new WebApplicationException("user not found", 404);
            }
            Usuario usuarioEditado = encontrado.get();
            updateFromTo(newUserState, usuarioEditado);
            context.save(usuarioEditado);
            return usuarioEditado;
        });


        return createTo(usuario);
    }

    private void updateFromTo(UserTo newUserState, Usuario usuarioEditado) {
        usuarioEditado.setPassword(newUserState.getPassword());
        usuarioEditado.setLogin(newUserState.getLogin());
        usuarioEditado.setName(newUserState.getName());
    }

    @DELETE
    @Path("/{userId}")
    public UserTo deleteUser(@PathParam("userId") Long userId){
        application.getHibernate().doInTransaction(context-> {
            context.perform(DeleteById.create(Usuario.class, userId));
            return null;
        });

        return UserTo.create(userId,"","","");
    }

    public static UserResource create(Application application) {
        UserResource resource = new UserResource();
        resource.application = application;
        return resource;
    }

}
