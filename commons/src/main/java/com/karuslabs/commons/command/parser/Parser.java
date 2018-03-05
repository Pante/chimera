/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
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

import com.karuslabs.commons.command.Command;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import static java.util.Collections.EMPTY_LIST;
import static java.util.stream.Collectors.toList;


/**
 * Represents a parser which creates {@code Command}s from a YAML document.
 * A reference for the YAML document syntax may be found in the GitHub <a href = "https://github.com/Pante/Karus-Commons/commands">wiki</a>/
 */
public class Parser {
    
    private Token<Command> command;
    
    
    /**
     * Constructs a {@code Parser} with the specified {@code Token} which creates
     * {@code Command}s.
     * 
     * @param command the token which creates Commands
     */
    public Parser(Token<Command> command) {
        this.command = command;
    }
    
    
    /**
     * Parses the specified {@code ConfigurationSection} and creates a list of {@code Command}s.
     * 
     * @param config the ConfigurationSection
     * @return a list of Commands
     */
    public List<Command> parse(ConfigurationSection config) {
        ConfigurationSection commands = config.getConfigurationSection("commands");
        if (commands != null) {
            return commands.getKeys(false).stream().map(key -> command.from(commands, key)).collect(toList());

        } else {
            return EMPTY_LIST;
        }
    }
    
    /**
     * Returns the {@code Token} which creates {@code Command}s.
     * 
     * @return the token
     */
    public Token<Command> getToken() {
        return command;
    }
    
}
