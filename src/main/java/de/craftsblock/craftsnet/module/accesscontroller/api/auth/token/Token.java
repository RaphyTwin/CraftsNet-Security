package de.craftsblock.craftsnet.module.accesscontroller.api.auth.token;

import de.craftsblock.craftscore.json.Json;
import de.craftsblock.craftscore.utils.id.Snowflake;
import de.craftsblock.craftsnet.module.accesscontroller.api.entity.Entity;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a token entity that holds information such as
 * the token ID, hash, expiration time, and associated permissions.
 * It also provides functionality for validation and serialization.
 *
 * @param id          the unique identifier of the token.
 * @param hash        the hashed value of the token secret.
 * @param expires     the expiration time of the token in milliseconds since epoch.
 * @param permissions a list of {@link TokenPermission}, defining access control rules for the token.
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @since 1.0.0
 */
public record Token(long id, String hash, long expires, List<TokenPermission> permissions) implements Entity {

    /**
     * Validates if the given secret matches the hashed secret stored in the token.
     *
     * @param secret the secret to be validated.
     * @return {@code true} if the secret matches the hash, {@code false} otherwise.
     */
    public boolean valid(String secret) {
        return BCrypt.checkpw(secret, hash());
    }

    /**
     * Converts the expiration time of the token from milliseconds since epoch
     * into an {@link OffsetDateTime} object.
     *
     * @return the {@link OffsetDateTime} representing the expiration time of the token.
     */
    public OffsetDateTime expiresAt() {
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(expires), OffsetDateTime.now().getOffset());
    }

    /**
     * Serializes the {@link Token} object into a {@link Json} object,
     * which includes the ID, hash, expiration time, and permission details.
     *
     * @return a {@link Json} object representing the serialized token.
     */
    @Override
    public Json serialize() {
        Json json = Json.empty();
        json.set("id", id);
        json.set("hash", hash);
        json.set("expires", expires);
        json.set("permissions", permissions.stream().map(TokenPermission::serialize).map(Json::getObject).toList());
        return json;
    }

    /**
     * Creates a {@link Token} object from a {@link Json} object.
     * The JSON must contain the token ID, hash, expiration time, and permission details.
     *
     * @param json the {@link Json} object containing the token data.
     * @return a new {@link Token} object based on the provided JSON data.
     */
    public static Token of(Json json) {
        return of(json.getLong("id"), json.getString("hash"), json.getLong("expires"),
                json.getJsonList("permissions").stream().map(TokenPermission::of).toList());
    }

    /**
     * Creates a new {@link  Token} object using a hash and expiration time.
     * The token ID is generated using the {@link Snowflake} utility.
     * The token will be created with empty permissions by default.
     *
     * @param hash    the hashed token secret.
     * @param expires the expiration time in milliseconds since epoch.
     * @return a new {@link Token} object.
     */
    public static Token of(String hash, long expires) {
        return of(Snowflake.generate(), hash, expires, new ArrayList<>());
    }

    /**
     * A private factory method for creating a {@link  Token} object with specified
     * ID, hash, expiration time, and permissions.
     *
     * @param id          the unique identifier of the token.
     * @param hash        the hashed token secret.
     * @param expires     the expiration time in milliseconds since epoch.
     * @param permissions a list of {@link TokenPermission} associated with this token.
     * @return a new {@link Token} object.
     */
    static Token of(long id, String hash, long expires, List<TokenPermission> permissions) {
        return new Token(id, hash, expires, permissions);
    }

}
