package de.craftsblock.cnet.modules.security.ratelimit;

import de.craftsblock.craftsnet.api.http.Exchange;
import de.craftsblock.craftsnet.api.http.Request;
import de.craftsblock.craftsnet.api.http.Response;
import de.craftsblock.craftsnet.api.utils.SessionStorage;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

/**
 * The {@link RateLimitAdapter} is an abstract class that defines the structure for rate limiting logic.
 * It enforces rate limiting policies for incoming {@link Request}s by mapping them to {@link RateLimitIndex} objects.
 * The adapter also manages configuration settings like maximum request count, expiration times, and response headers.
 * <p>
 * Subclasses must implement the {@link #adapt(Request, SessionStorage)} method to define custom rate limiting behavior.
 * </p>
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @see RateLimitIndex
 * @see RateLimitInfo
 * @see Request
 * @since 1.0.0-SNAPSHOT
 */
public abstract class RateLimitAdapter {

    /**
     * A {@link Pattern} used to validate adapter IDs. Only alphabetic characters are allowed.
     */
    public static final Pattern ID_CHECK = Pattern.compile("^[a-zA-Z]+$");

    /**
     * The maximum allowed expiration time in milliseconds (31 days).
     */
    public static final long MAX_EXPIRE_MILLIS = (long) 31 * 24 * 60 * 60 * 1000;

    private final String id;
    private final long max;
    private final long expire;
    private final boolean headers;

    /**
     * Constructs a new {@code RateLimitAdapter} with the specified ID and maximum requests.
     * The expiration time defaults to 60 seconds, and headers are included in the response.
     *
     * @param id  The ID of the adapter (must contain only alphabetic characters).
     * @param max The maximum number of requests allowed within the expiration period.
     * @throws IllegalStateException If the ID is invalid.
     * @see #RateLimitAdapter(String, long, long)
     */
    public RateLimitAdapter(String id, long max) {
        this(id, max, 1000 * 60);
    }

    /**
     * Constructs a new {@code RateLimitAdapter} with the specified ID, maximum requests, and expiration time.
     * Headers are included in the response by default.
     *
     * @param id     The ID of the adapter (must contain only alphabetic characters).
     * @param max    The maximum number of requests allowed within the expiration period.
     * @param expire The expiration time in milliseconds.
     * @throws IllegalStateException If the ID is invalid.
     * @see #RateLimitAdapter(String, long, long, boolean)
     */
    public RateLimitAdapter(String id, long max, long expire) {
        this(id, max, expire, true);
    }

    /**
     * Constructs a new {@code RateLimitAdapter} with the specified parameters.
     *
     * @param id      The ID of the adapter (must contain only alphabetic characters).
     * @param max     The maximum number of requests allowed within the expiration period.
     * @param expire  The expiration time in milliseconds (must be greater than 0 and less than or equal to {@link #MAX_EXPIRE_MILLIS}).
     * @param headers Whether the rate limiting headers should be included in the response.
     * @throws IllegalStateException If the ID is invalid.
     * @throws AssertionError        If the expiration time is not within the allowed range.
     */
    public RateLimitAdapter(String id, long max, long expire, boolean headers) {
        if (!ID_CHECK.matcher(id).matches())
            throw new IllegalStateException("Rate limiting adapter IDs may only contain letters! (Invalid ID: '" + id +
                    "', set for: " + getClass().getName() + ")");

        assert expire > 0 && expire <= MAX_EXPIRE_MILLIS;
        this.id = id.toUpperCase();
        this.max = max;
        this.expire = expire;
        this.headers = headers;
    }

    /**
     * Maps a {@link Request} to a {@link RateLimitIndex}, defining how rate limits are applied.
     * Subclasses must override this method to provide custom mapping logic.
     *
     * @param request The incoming HTTP request.
     * @param storage The session storage associated with the request.
     * @return A {@link RateLimitIndex} representing the rate limit for the request, or {@code null} if no rate limit applies.
     */
    public abstract @Nullable RateLimitIndex adapt(Request request, SessionStorage storage);

    /**
     * Creates a new {@link RateLimitInfo} instance for this adapter.
     *
     * @return A new {@link RateLimitInfo} instance.
     */
    public RateLimitInfo createInfo() {
        return RateLimitInfo.of(this);
    }

    /**
     * Appends rate limit information as HTTP headers to the response of the given {@link Exchange}.
     * <p>
     * This method adds the following headers to the response:
     * <ul>
     *     <li><code>X-RateLimit-Limit</code>: Indicates the maximum number of requests allowed within the rate limit.</li>
     *     <li><code>X-RateLimit-Remaining</code>: Indicates the remaining number of requests that can be made before the rate limit is exceeded.</li>
     *     <li><code>X-RateLimit-Reset</code>: Indicates the time in milliseconds until the rate limit resets.</li>
     * </ul>
     * </p>
     *
     * @param exchange The {@link Exchange} representing the current HTTP request and response.
     * @param info     The {@link RateLimitInfo} containing the rate limit details for the current request.
     */
    public void appendToResponse(final Exchange exchange, final RateLimitInfo info) {
        final Response response = exchange.response();

        response.addHeader("X-RateLimit-Limit", getId() + "=" + getMax());
        response.addHeader("X-RateLimit-Remaining", getId() + "=" + Math.max(0, getMax() - info.times().get()));
        response.addHeader("X-RateLimit-Reset", getId() + "=" + Math.max(0, info.expiresAt().get() - System.currentTimeMillis()));
    }

    /**
     * Indicates whether rate limiting information should be included in the response headers.
     *
     * @return {@code true} if headers should be included, {@code false} otherwise.
     */
    public boolean shouldBeInResponse() {
        return headers;
    }

    /**
     * Gets the ID of this adapter.
     *
     * @return The ID of the adapter.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the maximum number of requests allowed within the expiration period.
     *
     * @return The maximum number of requests.
     */
    public long getMax() {
        return max;
    }

    /**
     * Gets the expiration time in milliseconds for this rate limit.
     *
     * @return The expiration time in milliseconds.
     */
    public long getExpireInMilliseconds() {
        return expire;
    }

}
