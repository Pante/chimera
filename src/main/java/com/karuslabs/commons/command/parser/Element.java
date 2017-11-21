/*
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.karuslabs.commons.command.parser;

import java.util.Map;
import javax.annotation.Nonnull;

import org.bukkit.configuration.ConfigurationSection;


/**
 * Represent a key-value pair in an YAMl document which may produce a derived value from the value associated with this {@code Element}.
 * Keys and associated derived values may be declared and stored beforehand for reference further in the YAML document.
 *
 * @param <T> the type of the derived value
 */
public abstract class Element<T> {
    
    private final Map<String, T> declarations;
    
    
    /**
     * Constructs an {@code Element} with the specified declared values and associated derived values
     * for reference further in the YAML document.
     * 
     * @param declarations the declared values and associated derived values
     */
    public Element(@Nonnull Map<String, T> declarations) {
        this.declarations = declarations;
    }
    
    
    /**
     * Associates the key with the derived value from invoking {@link #parse(ConfigurationSection, String)}
     * with the specified {@code ConfigurationSection} and key, for reference further in the YAML
     * document.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     */
    public void declare(ConfigurationSection config, String key) {
        declarations.put(key, parse(config, key));
    }

    
    /**
     * Parsers the YAML value associated with the specified key in the specified {@code ConfigurationSection}
     * and produces a derived value.

     * The default implementation returns the value from invoking {@link #handleNull(ConfigurationSection, String)} if the YAML value is {@code null},
     * else returns the value from invoking {@link #handleDeclared(ConfigurationSection, String)} if the YAML value is a {@code String},
     * else returns the value from invoking {@link #handle(ConfigurationSection, String)} if {@link #check(ConfigurationSection, String)} is {@code true},
     * else throws a {@code ParserException}.
     * 
     * @param config the ConfigurationSection which contains the specified key-value pair
     * @param key the key
     * @return the derived value
     * @throws ParserException if this method fails to produce a derived value for the specified ConfigurationSection and key
     */
    public @Nonnull T parse(ConfigurationSection config, String key) {
        if (config.get(key) == null) {
            return handleNull(config, key);
            
        } else if (config.isString(key)) {
            return handleDeclared(config, key);
            
        } else if (check(config, key)) {
            return handle(config, key);
            
        } else {
            throw new ParserException("Invalid value for: " + config.getCurrentPath() + "." + key);
        }
    }
    
    /**
     * Returns a default value if the specified {@code ConfigurationSection} does not contain a value for the specified key.
     * This method is invoked internally by {@link #parse(ConfigurationSection, String)}. 

     * The default implementation throws a {@code ParserException}. 

     * Subclasses may override this method to customise the handling of {@code null} values.
     * 
     * @param config the ConfigurationSection which contains the specified key-value pair
     * @param key the key
     * @return a default value
     * @throws ParserException if this method invoked but not overridden
     */
    protected @Nonnull T handleNull(@Nonnull ConfigurationSection config, @Nonnull String key) {
        throw new ParserException("Missing key: " + config.getCurrentPath() + "." + key);
    }
    
    /**
     * Returns the derived value associated with the specified key, declared beforehand.
     * This method is invoked internally by {@link #parse(ConfigurationSection, String)}.

     * The default implementation returns derived value associated with the key from invoking {@link #getDeclaredKey(ConfigurationSection, String)}
     * with the specified {@code ConfigurationSection} and key, or throws a {@code ParserException} if this {@code Element} does not contain
     * a derived value for the key. 

     * Subclasses may override this method to customise the handling of declared values.
     * 
     * @param config the ConfigurationSection which contains the specified key-value pair
     * @param key the key
     * @return the derived value
     * @throws ParserException if this Element does not contain a mapping for the key
     */
    protected T handleDeclared(ConfigurationSection config, String key) {
        T declared = declarations.get(getDeclaredKey(config, key));
        if (declared != null) {
            return declared;
            
        } else {
            throw new ParserException("Missing declaration: " + config.getCurrentPath() + "." + key);
        }
    }
    
    /**
     * Returns a key which a declared derived value is associated with.
     * This method is invoked internally by {@link #parse(ConfigurationSection, String)}.

     * The default implementation returns the specified key.

     * Subclasses may override this method to customise the handling the production of declared keys.
     * 
     * @param config the ConfigurationSection which contains the specified key-value pair
     * @param key the key
     * @return the declared key
     */
    protected String getDeclaredKey(ConfigurationSection config, String key) {
        return key;
    }
    
    /**
     * Checks if the specified {@code ConfigurationSection} contains a valid value for the specified key.
     * 
     * @param config the ConfigurationSection which contains the specified key-value pair
     * @param key the key
     * @return true if the specified Configuration contains a valid value for the specified key; else false
     */
    protected abstract boolean check(@Nonnull ConfigurationSection config, @Nonnull String key);
    
    /**
     * Creates a derived value from the value associated with the specified key in 
     * the specified {@code ConfigurationSection}.
     * 
     * @param config the ConfigurationSection
     * @param key the key to which the YAML value is associated
     * @return the derived value
     */
    protected abstract @Nonnull T handle(@Nonnull ConfigurationSection config, @Nonnull String key);
    
    
    /**
     * Returns a {@code Map} which contains the keys and associated derived values for
     * reference further in the YAML document.
     * 
     * @return the keys and associated derived values
     */
    public Map<String, T> getDeclarations() {
        return declarations;
    }
    
}
