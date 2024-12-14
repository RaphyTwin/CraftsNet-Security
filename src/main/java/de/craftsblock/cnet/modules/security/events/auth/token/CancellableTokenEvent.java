package de.craftsblock.cnet.modules.security.events.auth.token;

import de.craftsblock.cnet.modules.security.auth.token.Token;
import de.craftsblock.craftscore.event.Cancellable;
import org.jetbrains.annotations.NotNull;


/**
 * Represents a cancellable token-related event.
 * <p>
 * This class extends {@link GenericTokenEvent} and implements {@link Cancellable},
 * allowing the event to be cancelled during processing.
 * </p>
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @see GenericTokenEvent
 * @see Cancellable
 * @since 1.0.0-SNAPSHOT
 */
public abstract class CancellableTokenEvent extends GenericTokenEvent implements Cancellable {

    private boolean cancelled = false;

    /**
     * Constructs a new {@code CancellableTokenEvent}.
     *
     * @param token The token associated with this event. Must not be null.
     * @throws NullPointerException If {@code token} is null.
     */
    public CancellableTokenEvent(@NotNull Token token) {
        super(token);
    }

    /**
     * Sets the cancellation state of this event.
     *
     * @param cancelled {@code true} to cancel the event, {@code false} to allow it to proceed.
     */
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Checks whether this event has been cancelled.
     *
     * @return {@code true} if the event is cancelled, {@code false} otherwise.
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

}
