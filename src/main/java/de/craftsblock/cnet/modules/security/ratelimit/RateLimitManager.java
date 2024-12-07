package de.craftsblock.cnet.modules.security.ratelimit;

import de.craftsblock.cnet.modules.security.utils.Manager;
import de.craftsblock.craftsnet.api.http.Exchange;
import de.craftsblock.craftsnet.api.http.Request;
import de.craftsblock.craftsnet.api.http.Response;
import de.craftsblock.craftsnet.api.utils.SessionStorage;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

/**
 * The {@link RateLimitManager} manages rate limiting adapters and their associated indices.
 * It handles the registration of adapters, checks for rate limiting conditions, and removes expired rate limit entries.
 * <p>
 * This class is thread-safe, using {@link ConcurrentHashMap} to store adapters and indices.
 * </p>
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @see RateLimitAdapter
 * @see RateLimitIndex
 * @see RateLimitInfo
 * @since 1.0.0-SNAPSHOT
 */
public class RateLimitManager implements Manager {

    private final ConcurrentHashMap<String, RateLimitAdapter> adapters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<RateLimitIndex, RateLimitInfo> indices = new ConcurrentHashMap<>();

    /**
     * Registers a {@link RateLimitAdapter} to this manager.
     *
     * @param adapter The {@link RateLimitAdapter} to register.
     * @throws IllegalStateException If an adapter with the same ID is already registered.
     */
    public void register(@NotNull RateLimitAdapter adapter) {
        String id = adapter.getId();
        if (adapters.containsKey(id))
            throw new IllegalStateException("Tried to register rate limit adapter with id " + id + " for " + adapter.getClass().getName() +
                    ", but this id is already taken by " + adapters.get(id).getClass().getName() + "!");

        this.adapters.put(id, adapter);
    }

    /**
     * Unregisters a {@link RateLimitAdapter} from this manager.
     *
     * @param adapter The {@link RateLimitAdapter} to unregister.
     */
    public void unregister(@NotNull RateLimitAdapter adapter) {
        this.adapters.remove(adapter.getId());
    }

    /**
     * Checks whether a {@link RateLimitAdapter} is registered with this manager.
     *
     * @param adapter The {@link RateLimitAdapter} to check.
     * @return {@code true} if the adapter is registered, {@code false} otherwise.
     */
    public boolean isRegistered(@NotNull RateLimitAdapter adapter) {
        return this.adapters.containsKey(adapter.getId());
    }

    /**
     * Determines whether the given {@link Exchange} is rate limited by any registered adapter.
     * If rate limited, the appropriate headers are added to the response.
     *
     * @param exchange The {@link Exchange} to check for rate limiting.
     * @return {@code true} if the request is rate limited, {@code false} otherwise.
     */
    public boolean isRateLimited(@NotNull Exchange exchange) {
        if (this.adapters.isEmpty()) return false;

        final Request request = exchange.request();
        final Response response = exchange.response();
        final SessionStorage storage = exchange.storage();

        AtomicBoolean limited = new AtomicBoolean(false);
        for (RateLimitAdapter adapter : adapters.values()) {
            RateLimitIndex index = adapter.adapt(request, storage);
            if (index == null) continue;

            RateLimitInfo info = indices.computeIfAbsent(index, r -> adapter.createInfo());
            boolean blocked = info.access();
            if (!limited.get()) limited.set(blocked);

            if (adapter.shouldBeInResponse()) {
                response.addHeader("X-RateLimit-Limit", adapter.getId() + "=" + adapter.getMax());
                response.addHeader("X-RateLimit-Remaining", adapter.getId() + "=" + Math.max(0, adapter.getMax() - info.times().get()));
                response.addHeader("X-RateLimit-Reset", adapter.getId() + "=" + Math.max(0, info.expiresAt().get() - System.currentTimeMillis()));
            }
        }

        return limited.get();
    }

    /**
     * Cleans up expired rate limit entries from the indices map.
     * This method uses parallel streams if the number of entries exceeds 100 for better performance.
     */
    public void tick() {
        Stream<Map.Entry<RateLimitIndex, RateLimitInfo>> stream;
        if (indices.size() >= 100) stream = indices.entrySet().parallelStream();
        else stream = indices.entrySet().stream();

        stream.filter(entry -> entry.getValue().isExpired())
                .forEach(entry -> indices.remove(entry.getKey()));
    }

}
