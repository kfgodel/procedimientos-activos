package ar.com.kfgodel.actions;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;
import ar.com.kfgodel.actions.descriptor.BuscadorDeFuncionesTipoAccion;
import convention.action.AccionDeTest;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Esta tipo verifica el comportamiento base del buscador
 * Created by kfgodel on 12/11/16.
 */
@RunWith(JavaSpecRunner.class)
public class BuscadorDeFuncionesTipoAccionTest extends JavaSpec<ActionTestContext> {
  @Override
  public void define() {
    describe("un buscador de tipo de accion por mensaje", () -> {
      context().buscadorAccion(BuscadorDeFuncionesTipoAccion::create);

      it("permite conocer todos los tipos de accion", () -> {
        List<Class<? extends Function>> tiposDeAccion = context().buscadorAccion().allTiposDeAccion();
        assertThat(tiposDeAccion).contains(AccionDeTest.class);
      });

      describe("cuando se busca para un mensaje", () -> {

        context().tipoEncontrado(() -> {
          Map<String, Object> mensaje = new HashMap<>();
          mensaje.put("recurso", context().nombreDeRecurso());
          return context().buscadorAccion().buscarTipoPara(mensaje);
        });

        it("permite obtener el tipo de accion que corresponde al recurso del mensaje", () -> {
          context().nombreDeRecurso(() -> "GET/test");
          assertThat(context().tipoEncontrado().get()).isEqualTo(AccionDeTest.class);
        });

        it("devuelve vacio si no encuentra el tipo de accion para el mensaje", () -> {
          context().nombreDeRecurso(() -> "no existo");
          assertThat(context().tipoEncontrado().isAbsent()).isTrue();
        });


      });


    });

  }
}