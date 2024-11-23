package de.craftsblock.cnet.modules.security.events.auth.token;

import de.craftsblock.cnet.modules.security.auth.token.Token;

public class TokenRevokeEvent extends GenericTokenEvent {
    public TokenRevokeEvent(Token token) {
        super(token);
    }
}
