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


public class CommandParser<T> extends Parser<T> {

    public static enum Label {
        NAME("name"), ALIAS("alias");
        
        private final String value;
        
        private Label(String value) {
            this.value = value;
        }
        
        public String toString() {
            return value;
        }
    }
    
    
    Map<String, Map.Entry<Command, Label>> names;
    Matcher matcher;
    
    
    public CommandParser(Environment<T> environment) {
        super(environment, Set.of(Commands.class, Command.class));
        names = new HashMap<>();
        matcher = COMMAND.matcher("");
    }
    
    
    @Override
    protected void parse(T type) {
        var commands = new HashMap<String, Object>();
        
        for (var command : environment.resolver.all(type, Command.class)) {
            check(type, command);
            commands.put(command.name(), parse(type, command));
        }
        
        environment.mappings.put("commands", commands);
    }
    
    protected void check(T type, Command command) {
        check(type, command, command.name(), NAME);
        for (var alias : command.aliases()) {
            check(type, command, alias, ALIAS);
        }
    }
    
    protected void check(T type, Command command, String name, Label label) {
        if (matcher.reset(name).matches()) {
            environment.error(type, format(name, "is not a valid command " + label, "should not contain whitespaces"));
            return;
            
        } else if (name.isEmpty()) {
            environment.error(type, "Command should not be empty");
            return;
        }
        
        var entry = names.get(name);
        if (entry == null) {
            names.put(name, new SimpleEntry<>(command, label));
            
        } else {
            environment.error(type, format(name, "already exists", "commands should not have the same aliases and "));
        }
    }
    
    
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
                environment.warning(type, format(command.permission(), "may be malformed"));
            }
            map.put("permission", command.permission());
        }
        
        if (!command.message().isEmpty()) {
            map.put("permission-message", command.message());
        }
        
        return map;
    }
    
    
    @Override
    protected void clear() {
        names.clear();
    }
    
}
