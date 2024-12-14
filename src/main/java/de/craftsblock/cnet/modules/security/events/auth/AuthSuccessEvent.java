package de.craftsblock.cnet.modules.security.events.auth;

import de.craftsblock.craftsnet.api.http.Exchange;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event triggered when authentication is successful.
 * <p>
 * This event extends {@link GenericAuthResultEvent} to provide
 * information about the successful authentication, such as the
 * associated {@link Exchange}.
 * </p>
 *
 * <p>Listeners can use this event to perform post-authentication actions,
 * such as logging or granting access to specific resources.</p>
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @since 1.0.0-SNAPSHOT
 */
public class AuthSuccessEvent extends GenericAuthResultEvent {

    /**
     * Constructs a new {@link AuthSuccessEvent}.
     *
     * @param exchange The HTTP exchange associated with the successful authentication.
     *                 Must not be null.
     * @throws NullPointerException If {@code exchange} is null.
     */
    public AuthSuccessEvent(@NotNull Exchange exchange) {
        super(exchange);
    }

}
