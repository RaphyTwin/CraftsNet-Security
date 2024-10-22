package de.craftsblock.craftsnet.module.accesscontroller.api.auth;

/**
 * The {@link AuthResult} class represents the outcome of an authentication process.
 * It provides information about whether the authentication was successful or cancelled,
 * and if cancelled, it holds a reason for the cancellation.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @since 1.0.0-SNAPSHOT
 */
public class AuthResult {

    private boolean success = true;
    private String cancelReason = "";

    /**
     * Creates a new {@link AuthResult} instance with a default success state of {@code true}.
     */
    public AuthResult() {
    }

    /**
     * Sets whether the authentication process was successful or not.
     * If set to {@code false}, the process is considered cancelled.
     *
     * @param success {@code true} if the authentication was successful, {@code false} if cancelled.
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Sets the reason for cancelling the authentication process.
     * This should be called when the authentication fails or is explicitly cancelled.
     *
     * @param cancelReason The reason for cancelling the authentication process.
     */
    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    /**
     * Returns whether the authentication was successful or not.
     *
     * @return {@code true} if the authentication was successful, {@code false} otherwise.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Returns whether the authentication process was cancelled.
     *
     * @return {@code true} if the process was cancelled, {@code false} otherwise.
     */
    public boolean isCancelled() {
        return !success;
    }

    /**
     * Returns the reason for cancelling the authentication process.
     * If the authentication was successful, this will return an empty string.
     *
     * @return The cancellation reason or an empty string if authentication was successful.
     */
    public String getCancelReason() {
        return cancelReason;
    }

}
