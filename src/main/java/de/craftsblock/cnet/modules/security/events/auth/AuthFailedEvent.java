package de.craftsblock.cnet.modules.security.events.auth;

import de.craftsblock.craftsnet.api.http.Exchange;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event triggered when authentication fails.
 * <p>
 * This event extends {@link GenericAuthResultEvent} to provide
 * information about the failed authentication attempt, such as the
 * associated {@link Exchange}.
 * </p>
 *
 * <p>Listeners can use this event to handle authentication failures,
 * such as logging the attempt or displaying an additional error message.</p>
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @since 1.0.0-SNAPSHOT
 */
public class AuthFailedEvent extends GenericAuthResultEvent {

    /**
     * Constructs a new {@link AuthFailedEvent}.
     *
     * @param exchange The HTTP exchange associated with the failed authentication.
     *                 Must not be null.
     * @throws NullPointerException If {@code exchange} is null.
     */
    public AuthFailedEvent(@NotNull Exchange exchange) {
        super(exchange);
    }

}
