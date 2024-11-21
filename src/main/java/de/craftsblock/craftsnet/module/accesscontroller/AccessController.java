package de.craftsblock.craftsnet.module.accesscontroller;

import de.craftsblock.craftscore.event.Event;
import de.craftsblock.craftsnet.module.accesscontroller.auth.AuthChainManager;
import de.craftsblock.craftsnet.module.accesscontroller.auth.token.TokenManager;
import de.craftsblock.craftsnet.module.accesscontroller.utils.Manager;

import java.lang.reflect.InvocationTargetException;
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

    /**
     * Dispatches the given event to the registered listeners via the listener registry.
     * This method ensures that the AccessController addon is active before proceeding.
     *
     * @param event The event to be dispatched to the listeners.
     * @throws IllegalStateException     If the AccessController addon is not active or not set.
     * @throws InvocationTargetException If an error occurs while invoking a listener method.
     * @throws IllegalAccessException    If a listener method cannot be accessed.
     */
    public static void callEvent(Event event) throws InvocationTargetException, IllegalAccessException {
        if (getControllerAddon() == null)
            throw new IllegalStateException("The addon instance has not been set! Is the AccessController addon active?");
        getControllerAddon().craftsNet().listenerRegistry().call(event);
    }

}
