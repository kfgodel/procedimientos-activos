package ar.dgarcia.prototipos.emberpoc;

import ar.com.kfgodel.webbyconvention.WebServer;

import java.util.Optional;

/**
 * Este tipo es el punto de entrada de la aplicación
 *  
 * Created by kfgodel on 19/02/15.
 */
public class EmberPocMain {
    
    public static void main(String[] args) throws Exception {
        WebServer.create(Optional.of(9090)).startAndJoin();
    }

}
