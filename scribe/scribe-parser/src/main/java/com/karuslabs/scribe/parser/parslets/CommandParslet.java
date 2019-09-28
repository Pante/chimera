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
package com.karuslabs.scribe.parser.parslets;

import com.karuslabs.scribe.parser.Parslet;

import java.util.*;
import java.util.regex.Matcher;
import javax.tools.Diagnostic.Kind;

import static com.karuslabs.scribe.parser.Parser.*;
import static javax.tools.Diagnostic.Kind.*;


public class CommandParslet implements Parslet {
    
    private Matcher matcher;
    
    
    public CommandParslet() {
        matcher = COMMAND.matcher("");
    }
    
    
    @Override
    public void parse(Map<String, Object> source, Map<String, Kind> errors) {
        var commands = (Map<String, Object>) source.get("commands");
        if (commands == null) {
            return;
        }
        
        var namespace = new HashSet<String>();
        namespace.addAll(commands.keySet());
        
        for (var command : commands.keySet()) {
            parseName(command, "name", errors);
        }
        
        for (var value : commands.values()) {
            var command = (Map<String, Object>) value;
            parseAliases(namespace, command, errors);
            parsePermission(command, errors);
        }
    }
    
    
    protected void parseName(String name, String type, Map<String, Kind> errors) {
        if (matcher.reset(name).matches()) {
            errors.put("Invalid command " + type + ": '" + name + "', " + type + " cannot contain whitespaces", ERROR);
            
        } else if (name.isBlank()) {
            errors.put("Invalid command " + type + ": '" + name + "', " + type + " cannot be empty", ERROR);
        }
    }
    
    
    protected void parseAliases(Set<String> namespace, Map<String, Object> command, Map<String, Kind> errors) {
        var value = command.get("aliases");
        if (value == null) {
            return;
        }
        
        if (value instanceof List<?>) {
            var aliases = (List<String>) value;
            for (var alias : aliases) {
                parseName(alias, "alias", errors);
                if (!namespace.add(alias)) {
                    errors.put("Conflicting command name and alias: '" + alias + "', names and aliases must be unique", ERROR);
                }
            }
            
        } else {
            errors.put("Invalid type for 'aliases', expected: 'List<String>, actual: '" +  value.getClass() + "'", ERROR);
        }
    }
    
    protected void parsePermission(Map<String, Object> command, Map<String, Kind> errors) {
        var value = command.get("permission");
        if (value == null) {
            return;
        }
        
        if (value instanceof String) {
            if (!PERMISSION.matcher((String) value).matches()) {
                errors.put("Potentially malformed command permission: '" + value + "'", WARNING);
            }
            
        } else {
            errors.put("Wrong type for 'permission', expected: 'String', actual: '" +  value.getClass() + "'", ERROR);
        }
    }
    
}
