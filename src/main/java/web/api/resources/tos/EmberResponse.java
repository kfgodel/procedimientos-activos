package web.api.resources.tos;

import java.util.HashMap;

/**
 * This type represents the ember envelope needed for ember-data to pick json data and map it to
 * correct types in the client side.<br>
 *     When communicating with ember, it expects a root object, with a property that maps to an model entity.<br>
 * <br>
 * It seems to conform to http://jsonapi.org/format/
 * Created by kfgodel on 03/03/15.
 */
public class EmberResponse extends HashMap<String, Object> {
    
    public static EmberResponse create(String emberModel, Object data) {
        EmberResponse wrapper = new EmberResponse();
        wrapper.put(emberModel, data);
        return wrapper;
    }

}
