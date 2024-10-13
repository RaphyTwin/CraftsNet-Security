package de.craftsblock.craftsnet.module.accesscontroller.api.auth.token;

import de.craftsblock.craftscore.json.Json;
import de.craftsblock.craftsnet.module.accesscontroller.api.entity.Entity;

import java.util.List;
import java.util.regex.Pattern;

/**
 * This class represents a set of permissions based on path and domain patterns,
 * encapsulated in a record with two lists of regular expression patterns.
 * The patterns control which paths and domains are allowed or wildcarded for
 * a given token.
 *
 * @param paths   a list of regular expression patterns representing allowed paths.
 * @param domains a list of regular expression patterns representing allowed domains.
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @since 1.0.0
 */
record TokenPermission(List<Pattern> paths, List<Pattern> domains) implements Entity {

    /**
     * Checks if any of the provided patterns are a wildcard pattern.
     * A pattern is considered a wildcard if it is "*" or ".*".
     *
     * @param patterns the list of patterns to check.
     * @return {@code true} if any pattern is a wildcard, {@code false} otherwise.
     */
    private boolean isWildcard(List<Pattern> patterns) {
        return patterns.stream().anyMatch(pattern -> pattern.pattern().equals("*") || pattern.pattern().equals(".*"));
    }

    /**
     * Determines if a given value is allowed by any pattern in the provided list.
     * The value is allowed if it matches any of the patterns.
     *
     * @param value    the string value to check.
     * @param patterns the list of patterns to match against.
     * @return {@code true} if the value matches any pattern, {@code false} otherwise.
     */
    private boolean isAllowed(String value, List<Pattern> patterns) {
        return patterns.stream().anyMatch(pattern -> pattern.matcher(value).matches());
    }

    /**
     * Checks if the path patterns include a wildcard pattern.
     *
     * @return {@code true} if the path patterns include a wildcard, {@code false} otherwise.
     */
    boolean isPathWildcard() {
        return isWildcard(paths());
    }

    /**
     * Determines if a given path is allowed based on the path patterns.
     * A path is allowed if it matches any of the patterns or if the patterns
     * include a wildcard.
     *
     * @param path the path to check.
     * @return {@code true} if the path is allowed, {@code false} otherwise.
     */
    boolean isPathAllowed(String path) {
        return isPathWildcard() || isAllowed(path, paths());
    }

    /**
     * Checks if the domain patterns include a wildcard pattern.
     *
     * @return {@code true} if the domain patterns include a wildcard, {@code false} otherwise.
     */
    boolean isDomainWildcard() {
        return isWildcard(domains());
    }

    /**
     * Determines if a given domain is allowed based on the domain patterns.
     * A domain is allowed if it matches any of the patterns or if the patterns
     * include a wildcard.
     *
     * @param domain the domain to check.
     * @return {@code true} if the domain is allowed, {@code false} otherwise.
     */
    boolean isDomainAllowed(String domain) {
        return isDomainWildcard() || isAllowed(domain, domains());
    }

    /**
     * Serializes the current {@link TokenPermission} object into a {@link Json} object.
     * The serialization includes converting the patterns into their string representation.
     *
     * @return a {@link Json} object containing the serialized paths and domains.
     */
    @Override
    public Json serialize() {
        return Json.empty()
                .set("paths", paths().stream().map(Pattern::pattern).toList())
                .set("domains", domains().stream().map(Pattern::pattern).toList());
    }

    /**
     * Constructs a {@link TokenPermission} object from a {@link Json} object.
     * The JSON should contain lists of paths and domains as regular expressions
     * in string format, which will be compiled into patterns.
     *
     * @param json the {@link Json} object containing the paths and domains.
     * @return a new {@link TokenPermission} object based on the provided JSON data.
     */
    static TokenPermission of(Json json) {
        return new TokenPermission(
                json.getStringList("paths").stream().map(Pattern::compile).toList(),
                json.getStringList("domains").stream().map(Pattern::compile).toList()
        );
    }

}
