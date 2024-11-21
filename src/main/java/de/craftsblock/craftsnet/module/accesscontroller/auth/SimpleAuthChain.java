package de.craftsblock.craftsnet.module.accesscontroller.auth;

import de.craftsblock.craftsnet.api.http.Exchange;
import de.craftsblock.craftsnet.api.http.Request;
import de.craftsblock.craftsnet.module.accesscontroller.auth.chains.AuthChain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private final ConcurrentLinkedQueue<AuthAdapter> adapters = new ConcurrentLinkedQueue<>();
    private final List<String> excluded = new ArrayList<>();

    /**
     * Authenticates the provided {@link Exchange} by passing it through the chain of
     * registered {@link AuthAdapter} instances. If any adapter in the chain cancels the
     * authentication, the process stops.
     *
     * @param exchange The {@link Exchange} object representing the incoming HTTP request.
     * @return The {@link AuthResult} object that contains the result of the authentication process.
     */
    @Override
    public AuthResult authenticate(final Exchange exchange) {
        final Request request = exchange.request();
        final AuthResult result = new AuthResult();

        Pattern pattern = Pattern.compile(String.join("|", excluded));
        Matcher matcher = pattern.matcher(request.getUrl());
        if (matcher.matches()) return result;

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

    /**
     * Adds a new url pattern to the exclusion list, preventing matching requests from undergoing authentication.
     *
     * @param pattern The exclusion pattern to add, typically a regex string matching URLs to exclude.
     */
    public void addExclusion(String pattern) {
        excluded.add(pattern);
    }

    /**
     * Removes an url pattern from the exclusion list, allowing matching requests to undergo authentication again.
     *
     * @param pattern The exclusion pattern to remove.
     */
    public void removeExclusion(String pattern) {
        excluded.remove(pattern);
    }


}
