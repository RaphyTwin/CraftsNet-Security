package de.craftsblock.cnet.modules.security.ratelimit;

import java.util.Objects;

/**
 * The {@link RateLimitIndex} class represents a unique index for rate limiting purposes.
 * It wraps an arbitrary {@link RateLimitIndex#source} object, which serves as the identifier for a rate limit.
 * <p>
 * This class is implemented as a record for immutability and concise representation.
 * </p>
 *
 * @param source The object representing the source of the rate limit.
 *               This could be an IP address, user ID, or any other identifier.
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @see Objects
 * @since 1.0.0-SNAPSHOT
 */
public record RateLimitIndex(Object source) {

    /**
     * Compares this {@link RateLimitIndex} with another object for equality.
     * Two {@link RateLimitIndex} instances are considered equal if their {@link #source} fields are equal.
     *
     * @param o The object to compare with this {@link RateLimitIndex}.
     * @return {@code true} if the objects are equal, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RateLimitIndex that = (RateLimitIndex) o;
        return Objects.equals(this.source, that.source);
    }

    /**
     * Computes the hash code for this {@link RateLimitIndex}.
     * The hash code is derived from the {@link #source} object.
     *
     * @return The hash code of this {@link RateLimitIndex}.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(source);
    }

    /**
     * Factory method to create a new {@link RateLimitIndex} instance.
     *
     * @param source The source object to use as the identifier for the rate limit.
     * @return A new {@link RateLimitIndex} instance wrapping the specified {@link RateLimitIndex#source}.
     */
    public static RateLimitIndex of(Object source) {
        return new RateLimitIndex(source);
    }

}
