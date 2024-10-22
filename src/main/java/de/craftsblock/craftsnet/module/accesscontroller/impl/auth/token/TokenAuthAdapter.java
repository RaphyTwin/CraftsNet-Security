package de.craftsblock.craftsnet.module.accesscontroller.impl.auth.token;

import de.craftsblock.craftsnet.api.http.Request;
import de.craftsblock.craftsnet.module.accesscontroller.AccessController;
import de.craftsblock.craftsnet.module.accesscontroller.api.auth.AuthAdapter;
import de.craftsblock.craftsnet.module.accesscontroller.api.auth.AuthResult;
import de.craftsblock.craftsnet.module.accesscontroller.api.auth.token.Token;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * The {@link TokenAuthAdapter} class implements the {@link AuthAdapter} interface to provide authentication
 * functionality using bearer tokens.
 * <p>
 * This adapter extracts the token from the Authorization header of a http request,
 * validates it, and performs authentication by checking the token's validity
 * against the stored tokens managed by the {@link TokenManager}.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @since 1.0.0-SNAPSHOT
 */
public class TokenAuthAdapter implements AuthAdapter {

    /**
     * The HTTP header name used for authorization.
     */
    public static final String AUTH_HEADER = "Authorization";

    /**
     * The expected authorization type for bearer tokens.
     */
    public static final String AUTH_TYPE = "bearer";

    /**
     * Authenticates the user based on the provided token in the request.
     * <p>
     * This method checks for the presence of the Authorization header and validates
     * the token format. If the token is valid, it retrieves the corresponding
     * {@link Token} from the {@link AccessController} and verifies the token's
     * secret using BCrypt. If any validation fails, the authentication result is
     * marked as failed.
     *
     * @param result  The {@link AuthResult} object where the authentication result will be stored.
     * @param request The {@link Request} object containing the HTTP request data.
     */
    @Override
    public void authenticate(AuthResult result, Request request) {
        // Retrieve the authorization header from the request
        String auth_header = request.getHeader(AUTH_HEADER);
        // Check if the header is present and if the auth type is correct
        if (auth_header == null || !auth_header.equalsIgnoreCase(AUTH_TYPE)) {
            failAuth(result, "Auth header not present or wrong auth type!");
            return;
        }

        // Extract the token from the authorization header
        String header = request.getHeaders().get(AUTH_HEADER).stream().filter(s -> s.startsWith("cnet_")).findFirst().orElse(null);
        // Check if a valid token is present
        if (header == null) {
            failAuth(result, "No valid auth token present!");
            return;
        }

        // Split the token into parts
        String[] parts = header.split("_");
        // Validate the number of parts in the token
        if (parts.length != 2) {
            failAuth(result, "No valid auth token present!");
            return;
        }

        try {
            // Extract the ID from the token
            String part = parts[1];
            if (part.length() <= 16) throw new IllegalStateException();
            long id = Long.parseLong(part.substring(0, 16), 16);

            // Retrieve the token from the token manager
            Token token = AccessController.getTokenManager().get(id);
            if (token == null) throw new IllegalStateException();

            // Extract the secret from the token and verify it
            String secret = part.substring(16);
            if (!BCrypt.checkpw(secret, token.hash())) throw new IllegalStateException();
        } catch (NumberFormatException | IllegalStateException e) {
            failAuth(result, "No valid auth token present!");
        }
    }

}
