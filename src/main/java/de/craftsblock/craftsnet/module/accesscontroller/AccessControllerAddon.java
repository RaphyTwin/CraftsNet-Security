package de.craftsblock.craftsnet.module.accesscontroller;

import de.craftsblock.craftsnet.addon.Addon;
import de.craftsblock.craftsnet.module.accesscontroller.common.listeners.PreRequestListener;
import de.craftsblock.craftsnet.module.accesscontroller.common.listeners.SocketListener;
import de.craftsblock.craftsnet.module.accesscontroller.impl.auth.AuthChainManager;
import de.craftsblock.craftsnet.module.accesscontroller.impl.auth.SimpleAuthChain;
import de.craftsblock.craftsnet.module.accesscontroller.impl.auth.token.TokenAuthAdapter;
import de.craftsblock.craftsnet.module.accesscontroller.impl.auth.token.TokenManager;

/**
 * The AccessControllerAddon class extends the base {@link Addon} class to provide specific functionality
 * for the access controller module.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @since 1.0.0-SNAPSHOT
 */
public class AccessControllerAddon extends Addon {

    /**
     * Called when the addon is loaded.
     */
    @Override
    public void onLoad() {
        // Set the instance
        AccessController.setControllerAddon(this);

        // Register listeners
        listenerRegistry().register(new PreRequestListener());
        listenerRegistry().register(new SocketListener());

        // Set environment variables
        AccessController.registerManager(new AuthChainManager());
        AccessController.registerManager(new TokenManager());
    }

    /**
     * Called when the addon is enabled.
     */
    @Override
    public void onEnable() {
        // Create a new default auth chain
        SimpleAuthChain authChain = new SimpleAuthChain();
        authChain.append(new TokenAuthAdapter());
        AccessController.getAuthChainManager().add(authChain);

    }

    /**
     * Called when the addon is disabled.
     */
    @Override
    public void onDisable() {
        AccessController.getTokenManager().save();

        // Last thing to execute
        AccessController.setControllerAddon(null);
    }

}
