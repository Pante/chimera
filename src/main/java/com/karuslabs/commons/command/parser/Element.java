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
 * Represent an element in an YAMl document which produces a value.
 * 
 * Elements may be associated with values and declared beforehand for further reference in the YAMl document.
 *
 * @param <T> the type of the value
 */
public abstract class Element<T> {
    
    private final Map<String, T> declarations;
    
    
    /**
     * Constructs an {@code Element} with the specified {@code Map} which contains the declared elements and associated values.
     * 
     * @param declarations the declared elements and associated values
     */
    public Element(@Nonnull Map<String, T> declarations) {
        this.declarations = declarations;
    }
    
    
    /**
     * Associates the specified key with the value from invoking {@link #parse(ConfigurationSection, String)}
     * on the specified {@code ConfigurationSection} and key.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     */
    public void declare(ConfigurationSection config, String key) {
        declarations.put(key, parse(config, key));
    }

    
    /**
     * Parses the specified key in the {@code ConfigurationSection} and produces a value.
     * 
     * The default implementation invokes {@link #handleNull(ConfigurationSection, String)} if the key value is {@code null};
     * else if the key is a {@code String}, invokes {@link #handleDeclared(ConfigurationSection, String)};
     * else if {@link #check(ConfigurationSection, String)} returns {@code true}, invokes {@link #handle(ConfigurationSection, String)};
     * else throws a {@code ParserException}.
     * 
     * @param config the ConfigurationSection which contains the specified key-value pair
     * @param key the key
     * @return the value
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
     * Returns a default value if the specified key in the {@code ConfigurationSection} is {@code null}.
     * This method is invoked by {@link #parse(ConfigurationSection, String)}. 
     * 
     * The default implementation throws a {@code ParserException}. 
     * 
     * Subclasses may override this method to customise the handling of {@code null} values.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     * @return a default value
     * @throws ParserException if this method invoked but not overridden
     */
    protected @Nonnull T handleNull(@Nonnull ConfigurationSection config, @Nonnull String key) {
        throw new ParserException("Missing key: " + config.getCurrentPath() + "." + key);
    }
    
    /**
     * Returns the value associated with the specified key, declared beforehand.
     * This method is invoked by {@link #parse(ConfigurationSection, String)}.

     * The default implementation returns the value associated with the key from invoking {@link #getDeclaredKey(ConfigurationSection, String)}
     * on the specified {@code ConfigurationSection} and key, or throws a {@code ParserException} if this {@code Element} does not contain
     * a value for the key. 

     * Subclasses may override this method to customise the handling of declared values.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     * @return the value
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
     * Returns a key to which a declared value is mapped.
     * This method is invoked by {@link #parse(ConfigurationSection, String)}.

     * The default implementation returns the specified key.

     * Subclasses may override this method to customise the handling of declared keys.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     * @return the declared key
     */
    protected String getDeclaredKey(ConfigurationSection config, String key) {
        return key;
    }
    
    /**
     * Checks if the specified key in the {@code ConfigurationSection} is valid.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     * @return true if the specified key is valid; else false
     */
    protected abstract boolean check(@Nonnull ConfigurationSection config, @Nonnull String key);
    
    /**
     * Creates a value for the specified key in the {@code ConfigurationSection}.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     * @return the value
     */
    protected abstract @Nonnull T handle(@Nonnull ConfigurationSection config, @Nonnull String key);
    
    
    /**
     * Returns a {@code Map} which contains the declared elements and their respective values.
     * 
     * @return the declared elements and their respective values
     */
    public Map<String, T> getDeclarations() {
        return declarations;
    }
    
}
