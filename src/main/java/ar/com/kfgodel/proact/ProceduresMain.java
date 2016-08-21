package ar.com.kfgodel.proact;

import ar.com.kfgodel.proact.config.ConfigurationSelector;
import ar.com.kfgodel.proact.config.HerokuPriorityConfigSelector;
import ar.com.kfgodel.proact.config.ProceduresConfiguration;

/**
 * Este tipo es el punto de entrada de la aplicación
 *  
 * Created by kfgodel on 19/02/15.
 */
public class ProceduresMain {
    
    public static void main(String[] args) {
        // Configuration depends on environment variables to detect if we are at heroku hosting
        ConfigurationSelector selector = HerokuPriorityConfigSelector.create();
        ProceduresConfiguration applicationConfig = selector.selectConfig();

        // Then proceed normally (heroku will connect to postgres, development uses local db)
        ProceduresApplication.create(applicationConfig).start();
    }

}
