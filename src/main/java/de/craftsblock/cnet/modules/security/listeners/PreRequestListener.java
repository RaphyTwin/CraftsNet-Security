package de.craftsblock.cnet.modules.security.listeners;

import de.craftsblock.cnet.modules.security.auth.AuthResult;
import de.craftsblock.cnet.modules.security.auth.chains.AuthChain;
import de.craftsblock.craftscore.event.EventHandler;
import de.craftsblock.craftscore.event.EventPriority;
import de.craftsblock.craftscore.event.ListenerAdapter;
import de.craftsblock.craftscore.json.Json;
import de.craftsblock.craftsnet.api.http.Exchange;
import de.craftsblock.craftsnet.events.requests.PreRequestEvent;
import de.craftsblock.cnet.modules.security.CNetSecurity;

import java.io.IOException;

/**
 * The PreRequestListener class listens for pre-request events and processes
 * authentication chains to determine if an incoming request should be allowed.
 */
public class PreRequestListener implements ListenerAdapter {

    /**
     * Handles the {@link PreRequestEvent}. This method is triggered when a pre-request
     * event occurs and processes the authentication chains.
     *
     * @param event The {@link PreRequestEvent} containing information about the request.
     * @throws IOException If an error occurs while processing the request or response.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void handlePreRequest(PreRequestEvent event) throws IOException {
        Exchange exchange = event.getExchange();

        // Iterate through each authentication chain
        for (AuthChain chain : CNetSecurity.getAuthChainManager()) {
            // Authenticate the incoming request using the current chain
            AuthResult result = chain.authenticate(exchange);

            // Check if the authentication was cancelled
            if (result.isCancelled()) {
                event.setCancelled(true); // Cancel the event
                // Send an error response back to the client
                exchange.response().print(Json.empty().set("error", result.getCancelReason()));
            }
        }
    }

}
