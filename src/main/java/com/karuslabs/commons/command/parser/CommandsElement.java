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
import com.karuslabs.commons.command.Command;

import java.util.*;
import javax.annotation.*;

import org.bukkit.configuration.ConfigurationSection;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;


/**
 * A concrete subclass of {@code Element} which creates a map associating 
 * the command names with the {@code Command}s from YAML elements.
 */
public class CommandsElement extends Element<Map<String, Command>> {

    private Element<Command> command;
    
    
    /**
     * Constructs a {@code CommandsElement} with the specified {@code Element} for
     * creating {@code Command}s and no declarations.
     * 
     * @param command the Element for creating Commands
     */
    public CommandsElement(@Nullable Element<Command> command) {
        this(command, new HashMap<>());
    }
    
    /**
     * Constructs a {@code CommandsElement} with the specified {@code Element} for
     * creating {@code Command}s and declarations.
     * 
     * @param command the Element for creating Commands
     * @param declarations the declarations
     */
    public CommandsElement(@Nullable Element<Command> command, Map<String, Map<String, Command>> declarations) {
        super(declarations);
        this.command = command;
    }
    
    
    /**
     * Creates an empty map.
     * 
     * @param config the ConfigurationSection
     * @param key th key
     * @return an empty map
     */
    @Override
    protected @Nonnull Map<String, Command> handleNull(@Ignored ConfigurationSection config, @Ignored String key) {
        return new HashMap<>(0);
    }
    
    /**
     * Checks if the value of the specified key in the {@code ConfigurationSection} is a {@code ConfigurationSection}.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     * @return true if the value is a ConfigurationSection; else false
     */
    @Override
    protected boolean check(@Nonnull ConfigurationSection config, @Nonnull String key) {
        return config.isConfigurationSection(key);
    }
    
    /**
     * Creates a map which associates the command names with the {@code Command}s from the 
     * value of the specified key in the {@code ConfigurationSection}.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     * @return a map which associates the command names with the Commands
     */
    @Override
    protected @Nonnull Map<String, Command> handle(@Nonnull ConfigurationSection config, @Nonnull String key) {
        ConfigurationSection subcommands = config.getConfigurationSection(key);
        return subcommands.getKeys(false).stream().collect(toMap(identity(), aKey -> {
            String declared = subcommands.getString(aKey);
            if (declared != null && declared.equals("declared")) {
                return command.handleDeclared(subcommands, aKey);
                
            } else {
                return command.parse(subcommands, aKey);
            }
        }));
    }

    
    /**
     * Returns the {@code Element} for creating {@code Command}s.
     * 
     * @return the element for creating Commands
     */
    public @Nullable Element<Command> getCommandElement() {
        return command;
    }
    
    /**
     * Sets the {@code Element} for creating {@code Command}s.
     * 
     * @param command the element for creating Commands
     */
    public void setCommandElement(Element<Command> command) {
        this.command = command;
    }
    
}
