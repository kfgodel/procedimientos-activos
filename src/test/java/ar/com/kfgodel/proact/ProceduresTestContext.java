package ar.com.kfgodel.proact;

import ar.com.dgarcia.javaspec.api.TestContext;
import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.appbyconvention.operation.api.chains.ChainedSessionOperation;
import ar.com.kfgodel.appbyconvention.operation.api.chains.ChainedTransactionOperation;
import ar.com.kfgodel.appbyconvention.operation.api.chains.SessionScopedOperation;
import ar.com.kfgodel.appbyconvention.operation.api.chains.TransactionScopedOperation;
import ar.com.kfgodel.proact.config.ConfigurationSelector;

import java.util.function.Supplier;

/**
 * This type represents the test context used for procedures tests
 * Created by kfgodel on 13/03/16.
 */
public interface ProceduresTestContext extends TestContext {

  ConfigurationSelector selector();
  void selector(Supplier<ConfigurationSelector> definition);

  ApplicationOperation operation();
  void operation(Supplier<ApplicationOperation> definition);

  SessionScopedOperation sessionScoped();
  void sessionScoped(Supplier<SessionScopedOperation> definition);

  TransactionScopedOperation transactionScoped();
  void transactionScoped(Supplier<TransactionScopedOperation> definition);


  ChainedSessionOperation<Integer[]> sessionChain();
  void sessionChain(Supplier<ChainedSessionOperation<Integer[]>> definition);

  ChainedTransactionOperation<Integer[]> transactionChain();
  void transactionChain(Supplier<ChainedTransactionOperation<Integer[]>> definition);
}
