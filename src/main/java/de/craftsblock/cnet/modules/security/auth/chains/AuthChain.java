package de.craftsblock.cnet.modules.security.auth.chains;

import de.craftsblock.cnet.modules.security.auth.AuthAdapter;
import de.craftsblock.cnet.modules.security.auth.AuthResult;
import de.craftsblock.craftsnet.api.http.Exchange;

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
     * Authenticates the provided {@link Exchange} by passing it through the chain of registered
     * {@link AuthAdapter} instances. Each adapter in the chain is responsible for determining
     * whether the request is authorized or not.
     *
     * @param exchange The {@link Exchange} object representing the incoming HTTP request.
     * @return The {@link AuthResult} object that contains the result of the authentication process.
     */
    public abstract AuthResult authenticate(Exchange exchange);

    /**
     * Appends a new {@link AuthAdapter} to the authentication chain. The adapter will be used
     * during future authentication attempts.
     *
     * @param adapter The {@link AuthAdapter} to be added to the authentication chain.
     * @return The instance of {@link AuthChain} used for chain method calls.
     */
    public abstract AuthChain append(AuthAdapter adapter);

    /**
     * Removes a specific {@link AuthAdapter} from the authentication chain.
     *
     * @param adapter The {@link AuthAdapter} to be removed from the authentication chain.
     * @return The instance of {@link AuthChain} used for chain method calls.
     */
    public abstract AuthChain remove(AuthAdapter adapter);

    /**
     * Removes all {@link AuthAdapter} instances of the specified type from the authentication chain.
     * This can be used to clear all adapters of a certain type (e.g., all token-based authenticators).
     *
     * @param adapter The class type of {@link AuthAdapter} to be removed.
     * @return The instance of {@link AuthChain} used for chain method calls.
     */
    public abstract AuthChain removeAll(Class<? extends AuthAdapter> adapter);

}
