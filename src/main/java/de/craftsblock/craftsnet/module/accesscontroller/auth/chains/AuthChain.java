package de.craftsblock.craftsnet.module.accesscontroller.auth.chains;

import de.craftsblock.craftsnet.api.http.Request;
import de.craftsblock.craftsnet.module.accesscontroller.auth.AuthAdapter;
import de.craftsblock.craftsnet.module.accesscontroller.auth.AuthResult;

/**
 * The {@link AuthChain} class represents an authentication chain that manages multiple
 * {@link AuthAdapter} instances. It provides methods to authenticate requests by passing
 * them through the chain of adapters and managing the adapters dynamically.
 *
 * <p>This class is designed to be extended for custom implementations of authentication chains,
 * where multiple authentication strategies (adapters) can be used in sequence.</p>
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @since 1.0.0-SNAPSHOT
 */
public abstract class AuthChain {

    /**
     * Authenticates the provided {@link Request} by passing it through the chain of registered
     * {@link AuthAdapter} instances. Each adapter in the chain is responsible for determining
     * whether the request is authorized or not.
     *
     * @param request The {@link Request} object representing the incoming HTTP request.
     * @return The {@link AuthResult} object that contains the result of the authentication process.
     */
    public abstract AuthResult authenticate(Request request);

    /**
     * Appends a new {@link AuthAdapter} to the authentication chain. The adapter will be used
     * during future authentication attempts.
     *
     * @param adapter The {@link AuthAdapter} to be added to the authentication chain.
     */
    public abstract void append(AuthAdapter adapter);

    /**
     * Removes a specific {@link AuthAdapter} from the authentication chain.
     *
     * @param adapter The {@link AuthAdapter} to be removed from the authentication chain.
     */
    public abstract void remove(AuthAdapter adapter);

    /**
     * Removes all {@link AuthAdapter} instances of the specified type from the authentication chain.
     * This can be used to clear all adapters of a certain type (e.g., all token-based authenticators).
     *
     * @param adapter The class type of {@link AuthAdapter} to be removed.
     */
    public abstract void removeAll(Class<? extends AuthAdapter> adapter);

}
