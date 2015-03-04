package ar.com.tenpines.html5poc;

import ar.com.kfgodel.webbyconvention.DefaultConfiguration;
import ar.com.kfgodel.webbyconvention.WebServer;

/**
 * Este tipo es el punto de entrada de la aplicación
 *  
 * Created by kfgodel on 19/02/15.
 */
public class Html5PocMain {
    
    public static void main(String[] args) throws Exception {
        WebServer.createFor(DefaultConfiguration.create().listeningHttpOn(9090))
                .startAndJoin();
    }

}
