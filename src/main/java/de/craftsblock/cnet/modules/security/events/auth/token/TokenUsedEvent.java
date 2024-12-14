package de.craftsblock.cnet.modules.security.events.auth.token;

import de.craftsblock.cnet.modules.security.auth.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * Event triggered when a token is successfully used.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @see GenericTokenEvent
 * @see Token
 * @since 1.0.0-SNAPSHOT
 */
public class TokenUsedEvent extends GenericTokenEvent {

    /**
     * Constructs a new {@link TokenUsedEvent}.
     *
     * @param token The token that has been used. Must not be null.
     * @throws NullPointerException If {@code token} is null.
     */
    public TokenUsedEvent(@NotNull Token token) {
        super(token);
    }

}
