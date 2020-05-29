/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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
package com.karuslabs.scribe.core.parsers;

import com.karuslabs.scribe.annotations.*;
import com.karuslabs.scribe.core.Environment;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.regex.Matcher;

import static com.karuslabs.annotations.processor.Messages.*;
import static com.karuslabs.scribe.core.parsers.CommandParser.Label.*;


/**
 * A parser that transforms a {@link Command} annotation into a {@code command}
 * section. 
 * <br>
 * <br>
 * The following constraints are enforced:
 * <ul>
 * <li>All command aliases and names must be neither empty nor contain whitespaces</li>
 * <li>All command aliases and names must be unique</li>
 * </ul>
 * <br>
 * In addition, a compile-time warning will be issued under the following circumstances:
 * <ul>
 * <li>A permission does not match {@code \w+(\.\w+)*(.\*)?}</li>
 * </ul>
 * 
 * @param <T> the annotated type
 */
public class CommandParser<T> extends Parser<T> {

    /**
     * Denotes if a string is an alias or name.
     */
    public static enum Label {
        NAME("name"), ALIAS("alias");
        
        private final String value;
        
        private Label(String value) {
            this.value = value;
        }
        
        @Override
        public String toString() {
            return value;
        }
    }
    
    
    Map<String, Map.Entry<Command, Label>> names;
    Matcher matcher;
    
    
    /**
     * Creates a {@code CommandParser} with the given environment.
     * 
     * @param environment the environment
     */
    public CommandParser(Environment<T> environment) {
        super(environment, Set.of(Commands.class, Command.class));
        names = new HashMap<>();
        matcher = COMMAND.matcher("");
    }
    
    
    /**
     * Validates, processes and adds the {@code @Command} annotation on {@code type} to the {@code commands} section
     * in {@link #resolution}. 
     * 
     * @param type the type
     */
    @Override
    protected void parse(T type) {
        var commands = new HashMap<String, Object>();
        
        for (var command : environment.resolver.all(type, Command.class)) {
            check(type, command);
            commands.put(command.name(), parse(type, command));
        }
        
        environment.mappings.put("commands", commands);
    }
    
    
    /**
     * Determines if the aliases and name of the given command satisfy the constraints.
     * 
     * @param type the annotated type
     * @param command the command
     */
    protected void check(T type, Command command) {
        check(type, command, command.name(), NAME);
        for (var alias : command.aliases()) {
            check(type, command, alias, ALIAS);
        }
    }
    
    /**
     * Determines if the given type, command, name and label satisfies the constraints.
     * 
     * @param type the annotated type
     * @param command the command 
     * @param name the name
     * @param label the label
     */
    protected void check(T type, Command command, String name, Label label) {
        if (matcher.reset(name).matches()) {
            environment.error(type, format(name, "is not a valid command " + label, "should not contain whitespaces"));
            return;
            
        } else if (name.isEmpty()) {
            environment.error(type, "Command " + label + " should not be empty");
            return;
        }
        
        var entry = names.get(name);
        if (entry == null) {
            names.put(name, new SimpleEntry<>(command, label));
            
        } else {
            environment.error(type, format(name, "already exists", "commands should not have the same aliases and names"));
        }
    }
    
    
    /**
     * Parses the given command and type to a {@code command} section.
     * 
     * @param type the annotated type
     * @param command the command
     * @return a command section
     */
    protected Map<String, Object> parse(T type, Command command) {
        var map = new HashMap<String, Object>();
        
        map.put("aliases", command.aliases());
        
        if (!command.description().isEmpty()) {
            map.put("description", command.description());
        }
        
        if (!command.syntax().isEmpty()) {
            map.put("usage", command.syntax());
        }
        
        if (!command.permission().isEmpty()) {
            if (!PERMISSION.matcher(command.permission()).matches()) {
                environment.warn(type, format(command.permission(), "may be malformed"));
            }
            map.put("permission", command.permission());
        }
        
        if (!command.message().isEmpty()) {
            map.put("permission-message", command.message());
        }
        
        return map;
    }
    
    
    /**
     * Clears the namespace for command aliases and names.
     */
    @Override
    protected void clear() {
        names.clear();
    }
    
}
