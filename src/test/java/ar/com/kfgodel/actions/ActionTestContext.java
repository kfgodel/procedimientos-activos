package ar.com.kfgodel.actions;

import ar.com.dgarcia.javaspec.api.contexts.TestContext;
import ar.com.kfgodel.nary.api.optionals.Optional;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This types gives shared context for action specs
 * Created by kfgodel on 12/11/16.
 */
public interface ActionTestContext extends TestContext {

  BuscadorDeTipoDeAccion buscadorAccion();

  void buscadorAccion(Supplier<BuscadorDeTipoDeAccion> definition);

  String nombreDeRecurso();

  void nombreDeRecurso(Supplier<String> definition);

  Optional<Class<? extends Function>> tipoEncontrado();

  void tipoEncontrado(Supplier<Optional<Class<? extends Function>>> definition);


}
