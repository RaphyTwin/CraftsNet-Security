package de.craftsblock.cnet.modules.security;

import de.craftsblock.cnet.modules.security.auth.AuthChainManager;
import de.craftsblock.cnet.modules.security.auth.chains.SimpleAuthChain;
import de.craftsblock.cnet.modules.security.auth.token.TokenAuthAdapter;
import de.craftsblock.cnet.modules.security.auth.token.TokenManager;
import de.craftsblock.cnet.modules.security.listeners.PreRequestListener;
import de.craftsblock.cnet.modules.security.listeners.SocketListener;
import de.craftsblock.cnet.modules.security.ratelimit.RateLimitManager;
import de.craftsblock.cnet.modules.security.ratelimit.builtin.IPRateLimitAdapter;
import de.craftsblock.cnet.modules.security.ratelimit.builtin.TokenRateLimitAdapter;
import de.craftsblock.craftsnet.addon.Addon;
import de.craftsblock.craftsnet.addon.meta.annotations.Meta;

/**
 * The AccessControllerAddon class extends the base {@link Addon} class to provide specific functionality
 * for the access controller module.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @since 1.0.0-SNAPSHOT
 */
@Meta(name = "CNetSecurity")
public class AddonEntrypoint extends Addon {

    /**
     * Called when the addon is loaded.
     */
    @Override
    public void onLoad() {
        // Set the instance
        CNetSecurity.register(this);
        CNetSecurity.register(this.logger());

        // Register listeners
        listenerRegistry().register(new PreRequestListener());
        listenerRegistry().register(new SocketListener());

        // Set environment variables
        CNetSecurity.register(new AuthChainManager());
        CNetSecurity.register(new TokenManager());
        CNetSecurity.register(new RateLimitManager());
    }

    /**
     * Called when the addon is enabled.
     */
    @Override
    public void onEnable() {
        // Create a new default auth chain
        AuthChainManager chains = CNetSecurity.getAuthChainManager();
        if (chains != null) {
            CNetSecurity.register(new SimpleAuthChain());
            CNetSecurity.getDefaultAuthChain().append(new TokenAuthAdapter());
        }

        // Insert built in rate limit adapters
        RateLimitManager rater = CNetSecurity.getRateLimitManager();
        if (rater != null) {
            rater.register(new IPRateLimitAdapter(this));
            rater.register(new TokenRateLimitAdapter(this));
        }
    }

    /**
     * Called when the addon is disabled.
     */
    @Override
    public void onDisable() {
        CNetSecurity.getTokenManager().save();

        // Unset the instance
        CNetSecurity.unregister(this);
    }

}
