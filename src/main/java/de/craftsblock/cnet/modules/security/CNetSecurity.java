package de.craftsblock.cnet.modules.security;

import de.craftsblock.cnet.modules.security.auth.AuthChainManager;
import de.craftsblock.cnet.modules.security.auth.chains.SimpleAuthChain;
import de.craftsblock.cnet.modules.security.ratelimit.RateLimitManager;
import de.craftsblock.cnet.modules.security.utils.Manager;
import de.craftsblock.craftscore.event.Event;
import de.craftsblock.cnet.modules.security.auth.token.TokenManager;

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
public class CNetSecurity {

    private static final ConcurrentHashMap<Class<?>, Object> instances = new ConcurrentHashMap<>();

    /**
     * Registers a new object instance in the internal map. The instance is stored using its class type as the key.
     * This method is intended to be used internally to register object instances during initialization.
     *
     * @param instance The object instance to be registered.
     */
    protected static void register(Object instance) {
        instances.put(instance.getClass(), instance);
    }

    /**
     * Unregisters an object instance from the internal map.
     *
     * @param instance The object instance to be unregistered.
     */
    protected static void unregister(Object instance) {
        unregister(instance.getClass());
    }

    /**
     * Unregisters an object type from the internal map.
     *
     * @param instance The object type to be unregistered.
     */
    protected static void unregister(Class<?> instance) {
        instances.remove(instance);
    }

    /**
     * Retrieves a registered manager instance by its class type.
     * If the requested manager has not been registered, an {@link IllegalStateException} is thrown.
     *
     * @param <T>  The type of the instance.
     * @param type class type of the instance to be retrieved.
     * @return The manager instance, if found.
     * @throws IllegalStateException If the manager instance is not registered.
     */
    protected static <T> T get(Class<T> type) {
        if (!instances.containsKey(AuthChainManager.class))
            throw new IllegalStateException("There is no instance of " + type.getSimpleName() + " registered!");
        return type.cast(instances.get(type));
    }

    /**
     * Retrieves the currently set {@link AddonEntrypoint} instance.
     *
     * @return The current {@link AddonEntrypoint}, or null if none has been set.
     */
    public static AddonEntrypoint getAddonEntrypoint() {
        return get(AddonEntrypoint.class);
    }

    /**
     * Retrieves the {@link TokenManager} instance that manages authentication tokens.
     *
     * @return The {@link TokenManager} instance.
     * @throws IllegalStateException If no instance of {@link TokenManager} is registered.
     */
    public static TokenManager getTokenManager() {
        return get(TokenManager.class);
    }

    /**
     * Retrieves the {@link AuthChainManager} instance that manages authentication chains.
     *
     * @return The {@link AuthChainManager} instance.
     * @throws IllegalStateException If no instance of {@link AuthChainManager} is registered.
     */
    public static AuthChainManager getAuthChainManager() {
        return get(AuthChainManager.class);
    }

    /**
     * Retrieves the {@link RateLimitManager} instance that manages rate limits.
     *
     * @return The {@link RateLimitManager} instance.
     * @throws IllegalStateException If no instance of {@link RateLimitManager} is registered.
     */
    public static RateLimitManager getRateLimitManager() {
        return get(RateLimitManager.class);
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
        if (getAddonEntrypoint() == null)
            throw new IllegalStateException("The addon instance has not been set! Is the CNetSecurity addon active?");
        getAddonEntrypoint().craftsNet().listenerRegistry().call(event);
    }

}
