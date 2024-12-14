package de.craftsblock.cnet.modules.security.events.auth.token;

import de.craftsblock.cnet.modules.security.auth.token.Token;
import de.craftsblock.cnet.modules.security.events.auth.GenericAuthEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a generic event related to authentication tokens.
 * <p>
 * This class serves as a base for more specific token related events
 * and provides access to the associated {@link Token}.
 * </p>
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @see GenericAuthEvent
 * @see Token
 * @since 1.0.0-SNAPSHOT
 */
public abstract class GenericTokenEvent extends GenericAuthEvent {

    private final @NotNull Token token;

    /**
     * Constructs a new {@link GenericTokenEvent}.
     *
     * @param token The token associated with this event. Must not be null.
     * @throws NullPointerException If {@code token} is null.
     */
    public GenericTokenEvent(@NotNull Token token) {
        this.token = token;
    }

    /**
     * Returns the token associated with this event.
     *
     * @return The associated {@link Token}, never null.
     */
    public @NotNull Token getToken() {
        return token;
    }

}
