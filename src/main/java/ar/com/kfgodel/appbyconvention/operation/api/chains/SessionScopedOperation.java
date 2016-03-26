package ar.com.kfgodel.appbyconvention.operation.api.chains;

import ar.com.kfgodel.appbyconvention.operation.api.chains.session.SessionInitiator;

/**
 * This type represents an application operation scoped to a session. All actions chained to this operation
 * will be executed under the scope of the same session
 * Created by kfgodel on 21/03/16.
 */
public interface SessionScopedOperation extends SessionInitiator {

}
