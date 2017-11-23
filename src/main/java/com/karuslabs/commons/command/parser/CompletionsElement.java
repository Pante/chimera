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

import com.karuslabs.commons.annotation.Ignored;
import com.karuslabs.commons.command.completion.Completion;

import java.util.*;
import javax.annotation.Nonnull;

import org.bukkit.configuration.ConfigurationSection;

import static java.util.stream.Collectors.toMap;


/**
 * A concrete subclass of {@code Element} that creates a {@code Map} which associates 
 * the indexes of arguments with {@code Completion}s from {@code completions} elements.
 */
public class CompletionsElement extends Element<Map<Integer, Completion>> {
    
    private Element<Completion> completion;
    
    
    /**
     * Constructs a {@code CompletionElmeent} with the specified {@code Element} for
     * creating {@code Completion}s and no declarations.
     * 
     * @param completion the Element for creating Completions
     */
    public CompletionsElement(Element<Completion> completion) {
        this(completion, new HashMap<>());
    }
    
    /**
     * Constructs a {@code CompletionElement} with the specified {@code Element} for
     * creating {@code Completion}s and declarations.
     * 
     * @param completion the Element for creating Completions
     * @param declarations the declarations
     */
    public CompletionsElement(Element<Completion> completion, Map<String, Map<Integer, Completion>> declarations) {
        super(declarations);
        this.completion = completion;
    }
    
    
    /**
     * Creates an empty {@code Map}.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     * @return an empty Map
     */
    @Override
    protected @Nonnull Map<Integer, Completion> handleNull(@Ignored ConfigurationSection config, @Nonnull String key) {
        return new HashMap<>();
    }
    
    /**
     * Checks if the value of the specified key in the {@code ConfigurationSection}
     * is a {@code ConfigurationSection}.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     * @return true if the value of the specified key in the ConfigurationSection is a ConfigurationSection; else false
     */
    @Override
    protected boolean check(@Nonnull ConfigurationSection config, @Nonnull String key) {
        return config.isConfigurationSection(key);
    }
    
    /**
     * Creates a {@code Map} which associates the indexes of arguments with {@code Completions}.
     * 
     * @param config
     * @param key
     * @return 
     */
    @Override
    protected @Nonnull Map<Integer, Completion> handle(@Nonnull ConfigurationSection config, @Nonnull String key) {
        ConfigurationSection completions = config.getConfigurationSection(key);
        return completions.getKeys(false).stream().collect(toMap(Integer::parseInt, each -> completion.parse(completions, each)));
    }
    
}
