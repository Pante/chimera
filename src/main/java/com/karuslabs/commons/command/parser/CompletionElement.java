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

import com.karuslabs.commons.command.completion.*;

import java.util.*;
import javax.annotation.Nonnull;

import org.bukkit.configuration.ConfigurationSection;

import static com.karuslabs.commons.command.completion.Completion.*;


/**
 * A concrete subclass of {@code Element} which creates {@code Completion}s.
 * 
 * This {@code CompletionElement} contains default declarations, mapping the literal names of {@link Completion#NONE},
 * {@link Completion#PLAYER_NAMES} and {@link Completion#WORLD_NAMES} to their respective {@code Completion}s.
 */
public class CompletionElement extends Element<Completion> {
    
    /**
     * Constructs a {@code CompletionElement} with the default declarations.
     */
    public CompletionElement() {
        this(new HashMap<>());
    }
    
    /**
     * Constructs a {@code CompletionElement} with the default and specified declarations.
     * 
     * @param declarations the declarations
     */
    public CompletionElement(Map<String, Completion> declarations) {
        super(declarations);
        declarations.put("NONE", NONE);
        declarations.put("PLAYER_NAMES", PLAYER_NAMES);
        declarations.put("WORLD_NAMES", WORLD_NAMES);
    }

    
    /**
     * Returns the value associated with the specified key in the {@code ConfigurationSection}.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     * @return the value associated with the specified key
     */
    @Override
    protected String getDeclaredKey(ConfigurationSection config, String key) {
        return config.getString(key);
    }
    
    /**
     * Checks if the specified key in the {@code ConfigurationSection} is a list.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     * @return true if the value associated with the specified key in the ConfigurationSection is a list; else false
     */
    @Override
    protected boolean check(@Nonnull ConfigurationSection config, @Nonnull String key) {
        return config.isList(key);
    }
    
    /**
     * Creates a {@code CachedCompletion} for the specified key in the {@code ConfigurationSection}.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     * @return a CachedCompletion
     */
    @Override
    protected @Nonnull CachedCompletion handle(@Nonnull ConfigurationSection config, @Nonnull String key) {
        return new CachedCompletion(config.getStringList(key));
    }
    
}
