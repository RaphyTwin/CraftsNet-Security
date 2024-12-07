package de.craftsblock.cnet.modules.security.ratelimit;

import java.util.concurrent.atomic.AtomicLong;

/**
 * The {@link RateLimitInfo} class encapsulates information about rate limiting for a specific {@link RateLimitAdapter}.
 * It tracks the number of accesses, the expiration time, and provides mechanisms to enforce rate limiting rules.
 * <p>
 * This record is immutable in structure, with thread-safe handling of internal state using {@link AtomicLong}.
 * </p>
 *
 * @param adapter   The {@link RateLimitAdapter} that defines the rate limiting configuration.
 * @param times     An {@link AtomicLong} tracking the number of accesses.
 * @param expiresAt An {@link AtomicLong} representing the expiration timestamp in milliseconds.
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @see RateLimitAdapter
 * @see AtomicLong
 * @since 1.0.0-SNAPSHOT
 */
public record RateLimitInfo(RateLimitAdapter adapter, AtomicLong times, AtomicLong expiresAt) {

    /**
     * Gets the expiration time as a new {@link AtomicLong}.
     * This prevents external modification of the internal expiration timestamp.
     *
     * @return A new {@link AtomicLong} representing the expiration timestamp.
     */
    public AtomicLong expiresAt() {
        return new AtomicLong(expiresAt.get());
    }

    /**
     * Attempts to access the resource controlled by this rate limit.
     * <p>
     * If the rate limit is expired, it resets the state. If the access count exceeds the maximum allowed,
     * the method returns {@code true}, indicating the rate limit has been exceeded. Otherwise, it increments
     * the access count and returns {@code false}.
     * </p>
     *
     * @return {@code true} if the rate limit is exceeded, {@code false} otherwise.
     */
    public boolean access() {
        if (resetIfExpired()) return false;
        if (times().get() >= adapter.getMax()) return true;

        times().incrementAndGet();
        return false;
    }

    /**
     * Checks if the rate limit has expired and resets it if necessary.
     *
     * @return {@code true} if the rate limit was expired and has been reset, {@code false} otherwise.
     */
    public boolean resetIfExpired() {
        if (isExpired()) {
            reset();
            return true;
        }

        return false;
    }

    /**
     * Resets the rate limit by setting the access count to zero and updating the expiration timestamp.
     */
    public void reset() {
        times().set(0);
        expiresAt.set(System.currentTimeMillis() + adapter().getExpireInMilliseconds());
    }

    /**
     * Checks whether the rate limit has expired based on the current system time.
     *
     * @return {@code true} if the rate limit has expired, {@code false} otherwise.
     */
    public boolean isExpired() {
        return expiresAt.get() <= System.currentTimeMillis();
    }

    /**
     * Creates a new {@link RateLimitInfo} instance with the specified {@link RateLimitAdapter}.
     * The access count and expiration timestamp are initialized and the state is reset.
     *
     * @param adapter The {@link RateLimitAdapter} to associate with this rate limit information.
     * @return A new {@link RateLimitInfo} instance.
     */
    public static RateLimitInfo of(RateLimitAdapter adapter) {
        RateLimitInfo info = new RateLimitInfo(adapter, new AtomicLong(-1), new AtomicLong(-1));
        info.reset();
        return info;
    }

}
