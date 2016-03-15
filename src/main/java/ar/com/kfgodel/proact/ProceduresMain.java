package ar.com.kfgodel.proact;

import ar.com.kfgodel.proact.config.ConfigurationSelector;
import ar.com.kfgodel.proact.config.HerokuConfigSelector;
import ar.com.kfgodel.proact.config.ProceduresConfiguration;

/**
 * Este tipo es el punto de entrada de la aplicación
 *  
 * Created by kfgodel on 19/02/15.
 */
public class ProceduresMain {
    
    public static void main(String[] args) {
        ConfigurationSelector selector = HerokuConfigSelector.create();
        ProceduresConfiguration applicationConfig = selector.selectConfig();
        ProceduresApplication.create(applicationConfig).start();
    }

}
