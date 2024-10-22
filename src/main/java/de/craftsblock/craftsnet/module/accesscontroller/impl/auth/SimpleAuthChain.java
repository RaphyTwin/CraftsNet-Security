package de.craftsblock.craftsnet.module.accesscontroller.impl.auth;

import de.craftsblock.craftsnet.api.http.Request;
import de.craftsblock.craftsnet.module.accesscontroller.api.auth.AuthAdapter;
import de.craftsblock.craftsnet.module.accesscontroller.api.auth.AuthResult;
import de.craftsblock.craftsnet.module.accesscontroller.api.auth.chains.AuthChain;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The {@link SimpleAuthChain} class is a concrete implementation of the {@link AuthChain} class,
 * using a simple queue-based approach to handle multiple {@link AuthAdapter} instances in sequence.
 * It processes each authentication adapter in the order they were added.
 *
 * <p>Adapters are executed in the order they were appended to the chain, and the chain stops
 * processing if an authentication result is cancelled (i.e., if an adapter denies access).</p>
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @since 1.0.0-SNAPSHOT
 */
public class SimpleAuthChain extends AuthChain {

    // A thread-safe queue holding the list of authentication adapters.
    private final ConcurrentLinkedQueue<AuthAdapter> adapters = new ConcurrentLinkedQueue<>();

    /**
     * Authenticates the provided {@link Request} by passing it through the chain of
     * registered {@link AuthAdapter} instances. If any adapter in the chain cancels the
     * authentication, the process stops.
     *
     * @param request The {@link Request} object representing the incoming HTTP request.
     * @return The {@link AuthResult} object that contains the result of the authentication process.
     */
    @Override
    public AuthResult authenticate(final Request request) {
        final AuthResult result = new AuthResult();

        // Iterate over each adapter in the chain and authenticate the request.
        for (AuthAdapter adapter : adapters) {
            adapter.authenticate(result, request);

            // Stop processing further adapters if the authentication is cancelled.
            if (result.isCancelled()) break;
        }

        return result;
    }

    /**
     * Appends a new {@link AuthAdapter} to the chain. If the adapter is already present,
     * it will not be added again.
     *
     * @param adapter The {@link AuthAdapter} to be appended to the chain.
     */
    @Override
    public void append(AuthAdapter adapter) {
        if (!adapters.isEmpty() && adapters.contains(adapter)) return;
        adapters.add(adapter);
    }

    /**
     * Removes a specific {@link AuthAdapter} from the chain.
     *
     * @param adapter The {@link AuthAdapter} to be removed from the chain.
     */
    @Override
    public void remove(AuthAdapter adapter) {
        adapters.remove(adapter);
    }

    /**
     * Removes all instances of the specified {@link AuthAdapter} class from the chain.
     *
     * @param adapter The class type of the {@link AuthAdapter} to be removed.
     */
    @Override
    public void removeAll(Class<? extends AuthAdapter> adapter) {
        adapters.stream()
                .filter(adapter::isInstance)
                .forEach(this::remove);
    }


}
