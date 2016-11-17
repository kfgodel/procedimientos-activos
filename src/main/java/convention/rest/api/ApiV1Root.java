package convention.rest.api;

import ar.com.kfgodel.proact.Application;

import javax.inject.Inject;
import javax.ws.rs.Path;

/**
 * This type represents the root resource for v1 api access.<br>
 * This allows multiple versions of the api in the same server (if needed)
 * *
 * Created by kfgodel on 03/03/15.
 */
@Path("/api/v1")
public class ApiV1Root {

  // Injected by jetty/jersey H2k internal binder (which only knwons the application)
  @Inject
  private Application application;

  private SessionResource session;
  private MessageResource messages;

  @Path("/session")
  public SessionResource session() {
    if (session == null) {
      session = SessionResource.create();
    }
    return session;
  }

  @Path("/messages")
  public MessageResource messages() {
    if (messages == null) {
      messages = application.getInjector().createInjected(MessageResource.class);
    }
    return messages;
  }

}
