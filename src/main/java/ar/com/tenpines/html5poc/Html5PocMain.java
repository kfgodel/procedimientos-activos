package ar.com.tenpines.html5poc;

import ar.com.tenpines.html5poc.config.ConfigurationSelector;
import ar.com.tenpines.html5poc.config.HerokuConfigSelector;
import ar.com.tenpines.html5poc.config.ProceduresConfiguration;

/**
 * Este tipo es el punto de entrada de la aplicación
 *  
 * Created by kfgodel on 19/02/15.
 */
public class Html5PocMain {
    
    public static void main(String[] args) {
        ConfigurationSelector selector = HerokuConfigSelector.create();
        ProceduresConfiguration applicationConfig = selector.selectConfig();
        ProceduresApplication.create(applicationConfig).start();
    }

}
