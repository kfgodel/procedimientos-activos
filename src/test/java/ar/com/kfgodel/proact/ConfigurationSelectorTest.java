package ar.com.kfgodel.proact;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;
import ar.com.kfgodel.proact.config.DevelopmentConfig;
import ar.com.kfgodel.proact.config.HerokuPriorityConfigSelector;
import ar.com.kfgodel.proact.config.ProceduresConfiguration;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This type verifies the behavior of a configuration selector
 * Created by kfgodel on 13/03/16.
 */
@RunWith(JavaSpecRunner.class)
public class ConfigurationSelectorTest extends JavaSpec<ProceduresTestContext> {
  @Override
  public void define() {
    describe("a config selector", () -> {
      context().selector(HerokuPriorityConfigSelector::create);

      it("picks the development config if no heroku environment variable is defined",()->{
        ProceduresConfiguration configuration = context().selector().selectConfig();

        assertThat(configuration).isInstanceOf(DevelopmentConfig.class);
      });
    });

  }
}