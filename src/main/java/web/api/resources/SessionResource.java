package web.api.resources;

import javax.ws.rs.GET;

/**
 * This type represents the user session as a resource that can be queried
 * Created by kfgodel on 08/02/16.
 */
public class SessionResource {

  /**
   * Used by the frontend to check if resources are available (authenticated)
   *
   * @return a hardcoded string just for the sake of returning something
   */
  @GET
  public String engageSession() {
    return "engaged";
  }

  public static SessionResource create() {
    SessionResource sessionResource = new SessionResource();
    return sessionResource;
  }

}
