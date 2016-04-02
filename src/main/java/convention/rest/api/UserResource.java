package convention.rest.api;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import ar.com.kfgodel.proact.Application;
import ar.com.kfgodel.proact.persistent.filters.users.FindAllUsersOrderedByName;
import ar.com.tenpines.orm.api.operations.basic.DeleteById;
import ar.com.tenpines.orm.api.operations.basic.FindById;
import ar.com.tenpines.orm.api.operations.basic.Save;
import convention.persistent.Usuario;
import convention.rest.api.tos.UserTo;

import javax.ws.rs.*;
import java.lang.reflect.Type;
import java.util.List;

/**
 * This type is the resource API for users
 * Created by kfgodel on 03/03/15.
 */
@Produces("application/json")
@Consumes("application/json")
public class UserResource {

  private static final Type LIST_OF_USER_TOS = new ReferenceOf<List<UserTo>>() {
  }.getReferencedType();

  private Application application;

  @GET
  public List<UserTo> getAllUsers() {
    return createOperation()
      .insideASession()
      .applying(FindAllUsersOrderedByName.create())
      .convertTo(LIST_OF_USER_TOS);
  }

  @POST
  public UserTo createUser() {
    return createOperation()
      .insideASession()
      .taking(Usuario.create("Sin nombre", "", ""))
      .applyingResultOf(Save::create)
      .convertTo(UserTo.class);
  }

  @GET
  @Path("/{userId}")
  public UserTo getSingleUser(@PathParam("userId") Long userId) {
    return createOperation()
      .insideASession()
      .applying(FindById.create(Usuario.class, userId))
      .mapping((encontrado) -> {
        // Answer 404 if missing
        return encontrado.orElseThrowRuntime(() -> new WebApplicationException("user not found", 404));
      })
      .convertTo(UserTo.class);
  }


  @PUT
  @Path("/{userId}")
  public UserTo updateUser(UserTo newUserState, @PathParam("userId") Long userId) {
    return createOperation()
      .insideATransaction()
      .taking(newUserState)
      .convertingTo(Usuario.class)
      .mapping((encontrado) -> {
        // Answer 404 if missing
        if (encontrado == null) {
          throw new WebApplicationException("user not found", 404);
        }
        return encontrado;
      }).applyingResultOf(Save::create)
      .convertTo(UserTo.class);
  }

  @DELETE
  @Path("/{userId}")
  public void deleteUser(@PathParam("userId") Long userId) {
    createOperation()
      .insideATransaction()
      .apply(DeleteById.create(Usuario.class, userId));
  }

  public static UserResource create(Application application) {
    UserResource resource = new UserResource();
    resource.application = application;
    return resource;
  }

  private ApplicationOperation createOperation() {
    return ApplicationOperation.createFor(application.getInjector());
  }

}
