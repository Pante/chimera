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
package com.karuslabs.scribe.declarative.resolvers;

import com.karuslabs.scribe.Resolver;
import com.karuslabs.scribe.declarative.*;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import javax.annotation.processing.Messager;

import static com.karuslabs.scribe.declarative.resolvers.CommandResolver.Type.*;
import static javax.tools.Diagnostic.Kind.*;


public class CommandResolver extends Resolver<Plugin> {
    
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
    public void resolve(Plugin plugin, Map<String, Object> map) {
        var matcher = COMMAND.matcher("");
        var commands = new HashMap<String, Object>();
        
        for (var command : plugin.commands()) {
            check(matcher, command);
            map.put(command.name(), resolve(command));
        }
        
        map.put("commands", commands);
    }
    
    
    protected void check(Matcher matcher, Command command) {
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
    
    
    protected Map<String, Object> resolve(Command command) {
        var map = new HashMap<String, Object>();
        
        map.put("aliases", command.aliases());
        
        if (command.description() != null) {
            map.put("description", command.description());
        }
        
        if (command.syntax() != null) {
            map.put("usage", command.syntax());
        }
        
        if (command.permission() != null) {
            if (!PERMISSION.matcher(command.permission()).matches()) {
                messager.printMessage(WARNING, "Potentially malformed command permission: " + command.permission());
            }
            map.put("permission", command.permission());
        }
        
        if (command.message() != null) {
            map.put("permission-message", command.message());
        }
        
        return map;
    }
    
    
    @Override
    public void close() {
        names.clear();
    }
    
}
