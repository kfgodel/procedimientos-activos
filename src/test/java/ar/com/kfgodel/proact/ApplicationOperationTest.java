package ar.com.kfgodel.proact;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.dependencies.impl.DependencyInjectorImpl;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import ar.com.kfgodel.proact.operation.ApplicationOperation;
import ar.com.kfgodel.proact.operation.ChainedSessionOperation;
import ar.com.kfgodel.proact.operation.ChainedTransactionOperation;
import ar.com.kfgodel.transformbyconvention.api.TypeTransformer;
import ar.com.kfgodel.transformbyconvention.impl.B2BTransformer;
import ar.com.kfgodel.transformbyconvention.impl.config.TransformerConfigurationByConvention;
import ar.com.tenpines.orm.api.HibernateOrm;
import ar.com.tenpines.orm.api.SessionContext;
import ar.com.tenpines.orm.api.TransactionContext;
import ar.com.tenpines.orm.api.operations.SessionOperation;
import ar.com.tenpines.orm.api.operations.TransactionOperation;
import org.assertj.core.util.Lists;
import org.junit.runner.RunWith;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_SMART_NULLS;
import static org.mockito.Mockito.mock;

/**
 * This type verifies the behavior and possible combinations for the application operation
 * Created by kfgodel on 24/03/16.
 */
@RunWith(JavaSpecRunner.class)
public class ApplicationOperationTest extends JavaSpec<ProceduresTestContext> {

  private static final Type LIST_OF_INTEGERS = new ReferenceOf<List<Integer>>() {
  }.getReferencedType();

  @Override
  public void define() {
    describe("an application operation", () -> {
      context().operation(() -> ApplicationOperation.createFor(new FakeOrm(), createTransformer()));

      it("can be done inside the context of a persistence session", () -> {
        assertThat(context().operation().insideASession())
          .isNotNull();
      });

      it("can be done inside the context of a persistence transaction", () -> {
        assertThat(context().operation().insideATransaction())
          .isNotNull();
      });

      describe("inside a session", () -> {
        context().sessionScoped(() -> context().operation().insideASession());

        it("can apply a session operation as the main action and get its result", () -> {
          SessionOperation<Integer> sessionOperation = (session) -> 3;

          Integer actionResult = context().sessionScoped()
            .apply(sessionOperation);

          assertThat(actionResult).isEqualTo(3);
        });

        it("can apply a session operation obtained from a supplier", () -> {
          SessionOperation<Integer> sessionOperation = (session) -> 3;

          Integer actionResult = context().sessionScoped()
            .applyResultFrom(() -> sessionOperation);

          assertThat(actionResult).isEqualTo(3);
        });

        it("starts a chained operation from a single session operation", () -> {
          SessionOperation<Integer> singleOperation = mock(SessionOperation.class, RETURNS_SMART_NULLS);

          ChainedSessionOperation<Integer> chained = context().sessionScoped()
            .applying(singleOperation);

          assertThat(chained).isNotNull();
        });

        it("starts a chained operation from a session operation supplier", () -> {
          Supplier<SessionOperation<Object>> supplier = mock(Supplier.class, RETURNS_SMART_NULLS);

          ChainedSessionOperation<Object> chained = context().sessionScoped()
            .applyingResultFrom(supplier);

          assertThat(chained).isNotNull();
        });

        it("starts a chained operation from a predefined value", () -> {
          Integer predefinedValue = 3;

          ChainedSessionOperation<Integer> chained = context().sessionScoped()
            .taking(predefinedValue);

          assertThat(chained).isNotNull();
        });

        it("starts a chained operation from a value supplier", () -> {
          Supplier<Object> valueSupplier = mock(Supplier.class, RETURNS_SMART_NULLS);

          ChainedSessionOperation<Object> chained = context().sessionScoped()
            .takingResultFrom(valueSupplier);

          assertThat(chained).isNotNull();
        });

        describe("when a chained operation is started", () -> {
          context().sessionChain(() -> context().sessionScoped()
            .applying((session) -> new Integer[]{3})
          );

          it("is executed when the result is asked", () -> {
            Integer[] result = context().sessionChain().get();

            assertThat(result).isEqualTo(new Integer[]{3});
          });

          it("extends the chain by adding a map from its result value to other value", () -> {
            ChainedSessionOperation<Long> extendedChain = context().sessionChain().mapping((previousOperationResult) -> {
              return 4L;
            });

            assertThat(extendedChain).isNotNull();
            assertThat(extendedChain).isNotSameAs(context().sessionChain());
          });

          it("extends the chain by adding a map from its result value to other operation", () -> {
            ChainedSessionOperation<Object> extendedChain = context().sessionChain().applyingResultOf((previousOperationResult) -> {
              return mock(SessionOperation.class, RETURNS_SMART_NULLS);
            });

            assertThat(extendedChain).isNotNull();
            assertThat(extendedChain).isNotSameAs(context().sessionChain());
          });

          it("extends the chain by adding a conversion to a type indicated by class", () -> {
            ChainedSessionOperation<String[]> extendedChain = context().sessionChain().convertingTo(String[].class);

            assertThat(extendedChain).isNotNull();
            assertThat(extendedChain).isNotSameAs(context().sessionChain());
          });

          it("extends the chain by adding a conversion to a generic type", () -> {
            ChainedSessionOperation<List<Integer>> extendedChain = context().sessionChain().convertingTo(LIST_OF_INTEGERS);

            assertThat(extendedChain).isNotNull();
            assertThat(extendedChain).isNotSameAs(context().sessionChain());
          });

          it("completes the chain and gets its result when a final map is done", () -> {
            Integer result = context().sessionChain().map((previousValue) -> {
              return previousValue[0] + 1;
            });

            assertThat(result).isEqualTo(4);
          });

          it("completes the chain and gets the result when a final operation is applied", () -> {
            Integer result = context().sessionChain().applyResultOf((previousValue) -> {
              SessionOperation<Integer> finalOperation = (session) -> previousValue[0] + 1;
              return finalOperation;
            });

            assertThat(result).isEqualTo(4);
          });

          it("completes the chain and gets the result when converting to a type indicated by class", () -> {
            String[] result = context().sessionChain().convertTo(String[].class);

            assertThat(result).isEqualTo(new String[]{"3"});
          });

          it("completes the chain and gets the result when converting to a generic type", () -> {
            List<Integer> result = context().sessionChain().convertTo(LIST_OF_INTEGERS);

            assertThat(result).isEqualTo(Lists.newArrayList(3));
          });

        });

      });


      describe("inside a transaction", () -> {
        context().transactionScoped(() -> context().operation().insideATransaction());

        it("can apply a transaction operation as the main action and get its result", () -> {
          TransactionOperation<Integer> transactionOperation = (transaction) -> 3;

          Integer actionResult = context().transactionScoped()
            .apply(transactionOperation);

          assertThat(actionResult).isEqualTo(3);
        });

        it("can apply a transaction operation obtained from a supplier", () -> {
          TransactionOperation<Integer> transactionOperation = (transaction) -> 3;

          Integer actionResult = context().transactionScoped()
            .applyResultFrom(() -> transactionOperation);

          assertThat(actionResult).isEqualTo(3);
        });

        it("starts a chained operation from a single transaction operation", () -> {
          TransactionOperation<Integer> singleOperation = mock(TransactionOperation.class, RETURNS_SMART_NULLS);

          ChainedTransactionOperation<Integer> chained = context().transactionScoped()
            .applying(singleOperation);

          assertThat(chained).isNotNull();
        });

        it("starts a chained operation from a transaction operation supplier", () -> {
          Supplier<TransactionOperation<Object>> supplier = mock(Supplier.class, RETURNS_SMART_NULLS);

          ChainedTransactionOperation<Object> chained = context().transactionScoped()
            .applyingResultFrom(supplier);

          assertThat(chained).isNotNull();
        });

        it("starts a chained operation from a predefined value", () -> {
          Integer predefinedValue = 3;

          ChainedTransactionOperation<Integer> chained = context().transactionScoped()
            .taking(predefinedValue);

          assertThat(chained).isNotNull();
        });

        it("starts a chained operation from a value supplier", () -> {
          Supplier<Object> valueSupplier = mock(Supplier.class, RETURNS_SMART_NULLS);

          ChainedTransactionOperation<Object> chained = context().transactionScoped()
            .takingResultFrom(valueSupplier);

          assertThat(chained).isNotNull();
        });

        describe("when a chained operation is started", () -> {
          context().transactionChain(() -> context().transactionScoped()
            .applying((transaction) -> new Integer[]{3})
          );

          it("is executed when the result is asked", () -> {
            Integer[] result = context().transactionChain().get();

            assertThat(result).isEqualTo(new Integer[]{3});
          });

          it("extends the chain by adding a map from its result value to other value", () -> {
            ChainedTransactionOperation<Long> extendedChain = context().transactionChain().mapping((previousOperationResult) -> {
              return 4L;
            });

            assertThat(extendedChain).isNotNull();
            assertThat(extendedChain).isNotSameAs(context().transactionChain());
          });

          it("extends the chain by adding a map from its result value to other operation", () -> {
            ChainedTransactionOperation<Object> extendedChain = context().transactionChain().applyingResultOf((previousOperationResult) -> {
              return mock(TransactionOperation.class, RETURNS_SMART_NULLS);
            });

            assertThat(extendedChain).isNotNull();
            assertThat(extendedChain).isNotSameAs(context().transactionChain());
          });

          it("extends the chain by adding a conversion to a type indicated by class", () -> {
            ChainedTransactionOperation<String[]> extendedChain = context().transactionChain().convertingTo(String[].class);

            assertThat(extendedChain).isNotNull();
            assertThat(extendedChain).isNotSameAs(context().transactionChain());
          });

          it("extends the chain by adding a conversion to a generic type", () -> {
            ChainedTransactionOperation<List<Integer>> extendedChain = context().transactionChain().convertingTo(LIST_OF_INTEGERS);

            assertThat(extendedChain).isNotNull();
            assertThat(extendedChain).isNotSameAs(context().transactionChain());
          });

          it("completes the chain and gets its result when a final map is done", () -> {
            Integer result = context().transactionChain().map((previousValue) -> {
              return previousValue[0] + 1;
            });

            assertThat(result).isEqualTo(4);
          });

          it("completes the chain and gets the result when a final operation is applied", () -> {
            Integer result = context().transactionChain().applyResultOf((previousValue) -> {
              TransactionOperation<Integer> finalOperation = (transaction) -> previousValue[0] + 1;
              return finalOperation;
            });

            assertThat(result).isEqualTo(4);
          });

          it("completes the chain and gets the result when converting to a type indicated by class", () -> {
            String[] result = context().transactionChain().convertTo(String[].class);

            assertThat(result).isEqualTo(new String[]{"3"});
          });

          it("completes the chain and gets the result when converting to a generic type", () -> {
            List<Integer> result = context().transactionChain().convertTo(LIST_OF_INTEGERS);

            assertThat(result).isEqualTo(Lists.newArrayList(3));
          });

        });

      });

    });
  }

  private TypeTransformer createTransformer() {
    DependencyInjector injector = DependencyInjectorImpl.create();
    injector.bindTo(HibernateOrm.class, new FakeOrm());
    TransformerConfigurationByConvention configuration = TransformerConfigurationByConvention.create(injector);
    return B2BTransformer.create(configuration);

  }

  private static class FakeOrm implements HibernateOrm {
    @Override
    public <R> R ensureSessionFor(SessionOperation<R> operation) {
      return operation.applyWithSessionOn(mock(SessionContext.class, RETURNS_SMART_NULLS));
    }

    @Override
    public <R> R ensureTransactionFor(TransactionOperation<R> operation) {
      return operation.applyWithTransactionOn(mock(TransactionContext.class, RETURNS_SMART_NULLS));
    }

    @Override
    public void close() {
      // Nothing to close
    }
  }
}