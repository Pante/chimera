/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
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
package com.karuslabs.scribe.annotations.resolvers;

import com.karuslabs.scribe.annotations.processor.Resolver;
import com.karuslabs.scribe.annotations.Command;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import javax.annotation.processing.Messager;

import static com.karuslabs.scribe.annotations.resolvers.CommandResolver.Type.*;
import javax.lang.model.element.Element;
import static javax.tools.Diagnostic.Kind.*;


public class CommandResolver extends Resolver {
    
    public static enum Type {
        NAME("name"), ALIAS("alias");
        
        public final String name;
        
        private Type(String type) {
            this.name = type;
        }
    }
    
    
    Map<String, Entry<Command, Type>> names;
    
    
    public CommandResolver(Messager messager) {
        super(messager);
        names = new HashMap<>();
    }

    
    @Override
    protected void resolve(Element element, Map<String, Object> results) {
        var matcher = COMMAND.matcher("");
        var commands = new HashMap<String, Object>();
        
        for (var command : element.getAnnotationsByType(Command.class)) {
            check(element, matcher, command);
            results.put(command.name(), resolve(element, command));
        }
        
        results.put("commands", commands);
    }
    
    
    protected void check(Element element, Matcher matcher, Command command) {
        check(matcher, command, command.name(), NAME);
        for (var alias : command.aliases()) {
            check(matcher, command, alias, ALIAS);
        }
    }
    
    protected void check(Matcher matcher, Command command, String name, Type type) {
        if (!matcher.reset(name).matches()) {
            messager.printMessage(ERROR, "Invalid command " + type.name + ": " + name + ", " + type.name + " cannot contain whitespaces");
        }
        
        var entry = names.get(command.name());
        if (entry == null) {
            names.put(command.name(), new SimpleEntry<>(command, type));
            
        } else if (type == NAME && entry.getValue() == NAME) {
            messager.printMessage(ERROR, "Conflicting command names: " + name + ", command names must be unique");
            
        } else if (type == ALIAS && entry.getValue() == ALIAS) {
            messager.printMessage(ERROR, "Conflicting command aliases: " + name + " for " + command.name() + " and " 
                                       + entry.getKey().name() + ", command aliases must be unique");
        } else {
            messager.printMessage(ERROR, "Conflicting command name and alias: " + name + " and alias for " + entry.getKey().name() + ", " 
                                       + ", command names and aliases must be unique");
        }
    }
    
    
    protected Map<String, Object> resolve(Element element, Command command) {
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
                messager.printMessage(MANDATORY_WARNING, "Potentially malformed command permission: " + command.permission(), element);
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
