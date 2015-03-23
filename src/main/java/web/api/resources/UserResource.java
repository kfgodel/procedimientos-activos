package web.api.resources;

import ar.com.tenpines.html5poc.Application;
import ar.com.tenpines.html5poc.persistent.Usuario;
import com.google.common.collect.Lists;
import web.api.resources.tos.EmberResponse;
import web.api.resources.tos.UserCredentialsTo;
import web.api.resources.tos.UserTo;

import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This type is the resource API for users
 * Created by kfgodel on 03/03/15.
 */
public class UserResource {

    private Application application;

    private static int nextId = 3;
    private static List<UserTo> users = Lists.newArrayList(
            UserTo.create(1L, "Pepito Gonzola", "pepe", "1234"),
            UserTo.create(2L, "AdminTerminator", "admin", "1234"));

    @GET
    public List<UserTo> getAllUsers(){
        List<Usuario> usuarios = application.getHibernate().doWithSession((context) ->
                        context.getSession().createCriteria(Usuario.class).list()
        );

        List<UserTo> userTos = new ArrayList<>(usuarios.size());
        for (Usuario usuario : usuarios) {
            UserTo userTo = createTo(usuario);
            userTos.add(userTo);
        }

        return userTos;
    }

    private UserTo createTo(Usuario usuario) {
        return UserTo.create(usuario.getId(), usuario.getName(), usuario.getLogin(), usuario.getPassword());
    }

    @POST
    public UserTo createUser(){
        Usuario nuevoUsuario = Usuario.create("Sin nombre", "", "");

        application.getHibernate().doInTransaction((context)-> context.getSession().save(nuevoUsuario));

        return createTo(nuevoUsuario);
    }

    @GET
    @Path("/{userId}")
    public UserTo getSingleUser(@PathParam("userId") Long userId){
        Usuario usuario = application.getHibernate().doWithSession(context -> (Usuario)context.getSession().get(Usuario.class, userId));
        if(usuario != null){
            return createTo(usuario);
        }
        throw new WebApplicationException("user not found", 404);
    }


    @PUT
    @Path("/{userId}")
    public UserTo editUser(UserTo newUserState, @PathParam("userId") Long userId){
        Usuario usuario = application.getHibernate().doInTransaction(context -> {
            Usuario usuarioEditado = (Usuario) context.getSession().get(Usuario.class, userId);
            if (usuarioEditado == null) {
                throw new WebApplicationException("user not found", 404);
            }
            usuarioEditado.setPassword(newUserState.getPassword());
            usuarioEditado.setLogin(newUserState.getLogin());
            usuarioEditado.setName(newUserState.getName());
            context.getSession().saveOrUpdate(usuarioEditado);
            return usuarioEditado;
        });


        return createTo(usuario);
    }

    @DELETE
    @Path("/{userId}")
    public UserTo deleteUser(@PathParam("userId") Long userId){
        Usuario usuarioBorrado = application.getHibernate().doInTransaction((context)-> {
            Usuario usuario = (Usuario) context.getSession().get(Usuario.class, userId);
            context.getSession().delete(usuario);
            return usuario;
        });

        return createTo(usuarioBorrado);
    }

    @POST
    @Path("/login")
    public EmberResponse login(UserCredentialsTo credentials){
        if(credentials.getLogin().equals("pepe") && credentials.getPassword().equals("1234")){
            return EmberResponse.create("user", UserTo.create(0L,"admin","pepe","1234"));
        }
        throw new WebApplicationException("Invalid credentials", 401);
    }

    public static UserResource create(Application application) {
        UserResource resource = new UserResource();
        resource.application = application;
        return resource;
    }

}
