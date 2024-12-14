package de.craftsblock.cnet.modules.security.events.auth.token;

import de.craftsblock.cnet.modules.security.auth.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * Event triggered before a token is revoked.
 * <p>
 * This event extends {@link CancellableTokenEvent}, allowing listeners to cancel the revocation process
 * if necessary. For example, cancellation might occur if the revocation request does not meet certain conditions
 * or is unauthorized.
 * </p>
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @see CancellableTokenEvent
 * @see Token
 * @since 1.0.0-SNAPSHOT
 */
public class TokenRevokeEvent extends CancellableTokenEvent {

    /**
     * Constructs a new {@link TokenRevokeEvent}.
     *
     * @param token The token being revoked. Must not be null.
     * @throws NullPointerException If {@code token} is null.
     */
    public TokenRevokeEvent(@NotNull Token token) {
        super(token);
    }

}
