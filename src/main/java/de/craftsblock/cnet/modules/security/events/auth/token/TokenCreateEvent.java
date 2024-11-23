package de.craftsblock.cnet.modules.security.events.auth.token;

import de.craftsblock.cnet.modules.security.auth.token.Token;

public class TokenCreateEvent extends GenericTokenEvent {

    public TokenCreateEvent(Token token) {
        super(token);
    }

}
