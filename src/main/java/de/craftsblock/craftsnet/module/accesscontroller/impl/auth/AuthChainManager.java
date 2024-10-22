package de.craftsblock.craftsnet.module.accesscontroller.impl.auth;

import de.craftsblock.craftsnet.module.accesscontroller.api.Manager;
import de.craftsblock.craftsnet.module.accesscontroller.api.auth.chains.AuthChain;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The {@code AuthChainManager} class is a manager for handling multiple {@link AuthChain} instances.
 * It extends {@link ConcurrentLinkedQueue} to provide a thread-safe way to manage and manipulate
 * authentication chains. Each {@link AuthChain} represents a chain of authentication adapters.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @since 1.0.0-SNAPSHOT
 */
public final class AuthChainManager extends ConcurrentLinkedQueue<AuthChain> implements Manager {

}
