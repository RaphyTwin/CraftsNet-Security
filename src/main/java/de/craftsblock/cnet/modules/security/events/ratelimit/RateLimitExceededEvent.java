package de.craftsblock.cnet.modules.security.events.ratelimit;

import de.craftsblock.cnet.modules.security.ratelimit.RateLimitAdapter;
import de.craftsblock.craftsnet.api.http.Exchange;

import java.util.List;

/**
 * The {@link RateLimitExceededEvent} is triggered when one or more rate limits are exceeded for a specific HTTP request.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @see GenericRateLimitEvent
 * @see RateLimitAdapter
 * @see Exchange
 * @since 1.0.0-SNAPSHOT
 */
public class RateLimitExceededEvent extends GenericRateLimitEvent {

    private final Exchange exchange;
    private final List<RateLimitAdapter> exceeded;

    /**
     * Constructs a new {@link RateLimitExceededEvent} with a given {@link Exchange} and a variable number of {@link RateLimitAdapter}s.
     *
     * @param exchange The {@link Exchange} representing the HTTP request that caused the rate limit to be exceeded.
     * @param exceeded A variable number of {@link RateLimitAdapter}s responsible for exceeding the rate limits.
     */
    public RateLimitExceededEvent(Exchange exchange, RateLimitAdapter... exceeded) {
        this(exchange, List.of(exceeded));
    }

    /**
     * Constructs a new {@link RateLimitExceededEvent} with a given {@link Exchange} and a list of {@link RateLimitAdapter}s.
     *
     * @param exchange The {@link Exchange} representing the HTTP request that caused the rate limit to be exceeded.
     * @param exceeded A list of {@link RateLimitAdapter}s responsible for exceeding the rate limits.
     */
    public RateLimitExceededEvent(Exchange exchange, List<RateLimitAdapter> exceeded) {
        this.exchange = exchange;
        this.exceeded = exceeded;
    }

    /**
     * Gets the {@link Exchange} associated with this event.
     *
     * @return The {@link Exchange} associated with the rate limiting event.
     */
    public Exchange getExchange() {
        return exchange;
    }

    /**
     * Gets the list of {@link RateLimitAdapter}s responsible for the rate limit being exceeded.
     *
     * @return A list of {@link RateLimitAdapter}s that exceeded their limits.
     */
    public List<RateLimitAdapter> getExceeded() {
        return exceeded;
    }

}
