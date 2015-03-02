package ar.com.kfgodel.web.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by kfgodel on 19/02/15.
 */
@Path("test")
public class TestResource {
    
    @GET
    public String get(){
        return "hola";
    }
}
