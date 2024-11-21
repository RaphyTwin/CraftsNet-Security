package de.craftsblock.craftsnet.module.accesscontroller.auth.token;

import com.google.gson.JsonElement;
import de.craftsblock.craftscore.json.Json;
import de.craftsblock.craftscore.json.JsonParser;
import de.craftsblock.craftsnet.module.accesscontroller.AccessController;
import de.craftsblock.craftsnet.module.accesscontroller.utils.Manager;
import de.craftsblock.craftsnet.utils.Utils;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages a collection of authentication tokens, providing functionality to register, unregister, save,
 * and generate tokens with associated permissions. It extends {@link ConcurrentHashMap} to store tokens
 * by their unique IDs and implements the {@link Manager} interface for managing token-related operations.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @since 1.0.0-SNAPSHOT
 */
public final class TokenManager extends ConcurrentHashMap<Long, Token> implements Manager {

    private final File saveFile;

    /**
     * Constructs a new {@link TokenManager} and loads tokens from the save file.
     * The tokens are stored in a JSON file located in the addon's data folder.
     * If the file contains a valid json array, tokens are deserialized and loaded into the manager.
     */
    public TokenManager() {
        saveFile = new File(AccessController.getControllerAddon().getDataFolder(), "tokens.json");
        Json json = JsonParser.parse(saveFile);
        if (!json.getObject().isJsonArray()) return;

        for (JsonElement element : json.getObject().getAsJsonArray()) {
            Token token = Token.of(JsonParser.parse(element));
            this.put(token.id(), token);
        }
    }

    /**
     * Registers a new token by adding it to the token manager.
     *
     * @param token The {@link Token} to be registered.
     */
    public void registerToken(Token token) {
        this.put(token.id(), token);
    }

    /**
     * Unregisters a token by removing it from the token manager.
     *
     * @param token The {@link Token} to be unregistered.
     */
    public void unregisterToken(Token token) {
        this.remove(token.id());
    }

    /**
     * Saves the current tokens in the token manager to a json file. The file is stored
     * in the addon's data folder. All tokens are serialized and saved as a json array.
     */
    public void save() {
        Json json = Json.empty();
        this.values().forEach(token -> json.set("$new", token.serialize()));
        json.save(saveFile);
    }

    /**
     * Generates a new token with the provided permissions, creates a random secret,
     * hashes the secret using BCrypt, and associates the permissions with the token.
     *
     * @param permissions An array of {@link TokenPermission} to be associated with the token.
     * @return A {@link Map.Entry} containing the plain text secret (as the key) and the generated {@link Token} (as the value).
     */
    public Map.Entry<String, Token> generateToken(TokenPermission... permissions) {
        return generateToken(Arrays.asList(permissions));
    }

    /**
     * Generates a new token with the provided list of permissions, creates a random secret,
     * hashes the secret using BCrypt, and associates the permissions with the token.
     *
     * @param permissions A list of {@link TokenPermission} to be associated with the token.
     * @return A {@link Map.Entry} containing the plain text secret (as the key) and the generated {@link Token} (as the value).
     */
    public Map.Entry<String, Token> generateToken(List<TokenPermission> permissions) {
        try {
            String secret = Utils.secureRandomPassphrase(45, 70, false);
            String hash = BCrypt.hashpw(secret, BCrypt.gensalt());

            Token token = Token.of(hash);
            token.permissions().addAll(permissions);
            put(token.id(), token);

            return Map.entry("cnet_" + Long.toHexString(token.id()) + secret, token);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
