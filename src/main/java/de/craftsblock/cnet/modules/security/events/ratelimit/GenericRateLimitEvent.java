package de.craftsblock.cnet.modules.security.events.ratelimit;

import de.craftsblock.craftscore.event.Event;

/**
 * The {@link GenericRateLimitEvent} serves as a base class for all rate-limiting-related events.
 * <p>
 * Subclasses of this event can be used to handle various rate-limiting scenarios,
 * such as when a rate limit is exceeded or reset.
 * </p>
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @see Event
 * @since 1.0.0-SNAPSHOT
 */
public abstract class GenericRateLimitEvent extends Event {

    /**
     * Constructs a new {@link GenericRateLimitEvent}.
     */
    public GenericRateLimitEvent() {

    }

}
