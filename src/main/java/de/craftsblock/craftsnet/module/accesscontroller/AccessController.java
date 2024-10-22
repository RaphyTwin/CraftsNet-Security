package de.craftsblock.craftsnet.module.accesscontroller;

import de.craftsblock.craftsnet.module.accesscontroller.api.Manager;
import de.craftsblock.craftsnet.module.accesscontroller.impl.auth.AuthChainManager;
import de.craftsblock.craftsnet.module.accesscontroller.impl.auth.token.TokenManager;

import java.util.concurrent.ConcurrentHashMap;

/**
 * The AccessController class provides functionality for managing various variables used by the access control addon.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @since 1.0.0-SNAPSHOT
 */
public class AccessController {

    private static AddonEntrypoint controllerAddon;

    private static final ConcurrentHashMap<Class<? extends Manager>, Manager> managers = new ConcurrentHashMap<>();

    /**
     * Sets the AccessControllerAddon for this AccessController.
     * This method is intended to be used internally by the system.
     *
     * @param controllerAddon The instance of {@link AddonEntrypoint} to be set.
     */
    protected static void setControllerAddon(AddonEntrypoint controllerAddon) {
        AccessController.controllerAddon = controllerAddon;
    }

    /**
     * Retrieves the currently set {@link AddonEntrypoint} instance.
     *
     * @return The current {@link AddonEntrypoint}, or null if none has been set.
     */
    public static AddonEntrypoint getControllerAddon() {
        return controllerAddon;
    }

    /**
     * Registers a new manager instance in the internal map. The manager is stored using its class type as the key.
     * This method is intended to be used internally to register managers during initialization.
     *
     * @param <T>     The type of the manager, extending {@link Manager}.
     * @param manager The manager instance to be registered.
     */
    protected static <T extends Manager> void registerManager(T manager) {
        managers.put(manager.getClass(), manager);
    }

    /**
     * Retrieves a registered manager instance by its class type.
     * If the requested manager has not been registered, an {@link IllegalStateException} is thrown.
     *
     * @param <T>     The type of the manager, extending {@link Manager}.
     * @param manager The class type of the manager to be retrieved.
     * @return The manager instance, if found.
     * @throws IllegalStateException If the manager instance is not registered.
     */
    protected static <T extends Manager> T getManager(Class<T> manager) {
        if (!managers.containsKey(AuthChainManager.class))
            throw new IllegalStateException("There is no instance of " + manager.getSimpleName() + " registered!");
        return manager.cast(managers.get(manager));
    }

    /**
     * Retrieves the {@link TokenManager} instance that manages authentication tokens.
     *
     * @return The {@link TokenManager} instance.
     * @throws IllegalStateException If no instance of {@link TokenManager} is registered.
     */
    public static TokenManager getTokenManager() {
        return getManager(TokenManager.class);
    }

    /**
     * Retrieves the {@link AuthChainManager} instance that manages authentication chains.
     *
     * @return The {@link AuthChainManager} instance.
     * @throws IllegalStateException If no instance of {@link AuthChainManager} is registered.
     */
    public static AuthChainManager getAuthChainManager() {
        return getManager(AuthChainManager.class);
    }

}
