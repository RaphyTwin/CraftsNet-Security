package de.craftsblock.craftsnet.module.accesscontroller.utils;

import de.craftsblock.craftscore.json.Json;

/**
 * This interface defines the contract for any class that can be serialized
 * into a {@link Json} object. It serves as a common type for entities
 * that need to be converted to JSON format.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Entity {

    /**
     * Serializes the current object into a {@link Json} representation.
     * Implementing classes should define how their internal state is converted
     * into a JSON format.
     *
     * @return a {@link Json} object representing the serialized state of the entity.
     */
    Json serialize();

}
