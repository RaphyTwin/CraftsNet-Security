package de.craftsblock.cnet.modules.security.events.auth.token;

import de.craftsblock.cnet.modules.security.auth.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * Event triggered before a new token is created.
 * <p>
 * This event extends {@link CancellableTokenEvent}, allowing listeners to cancel the token creation process
 * if necessary. Cancellation might be useful in cases where certain conditions for token creation
 * are not met.
 * </p>
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @see CancellableTokenEvent
 * @see Token
 * @since 1.0.0-SNAPSHOT
 */
public class TokenCreateEvent extends CancellableTokenEvent {

    /**
     * Constructs a new {@link TokenCreateEvent}.
     *
     * @param token The token being created. Must not be null.
     * @throws NullPointerException If {@code token} is null.
     */
    public TokenCreateEvent(@NotNull Token token) {
        super(token);
    }

}
