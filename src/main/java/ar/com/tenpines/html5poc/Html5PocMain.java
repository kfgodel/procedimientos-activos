package ar.com.tenpines.html5poc;

import ar.com.tenpines.html5poc.config.DevelopmentConfig;

/**
 * Este tipo es el punto de entrada de la aplicación
 *  
 * Created by kfgodel on 19/02/15.
 */
public class Html5PocMain {
    
    public static void main(String[] args) {
        ProceduresApplication.create(DevelopmentConfig.create()).start();
    }

}
