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
import javax.tools.Diagnostic.Kind;

import static com.karuslabs.scribe.parser.Parser.PERMISSION;
import static javax.tools.Diagnostic.Kind.*;


public class CommandParslet implements Parslet {
    
    @Override
    public void parse(Map<String, Object> source, Map<String, Kind> results) {
        var commands = (Map<String, Object>) source.get("commands");
        if (commands == null) {
            return;
        }
        
        var namespace = new HashSet<String>();
        namespace.addAll(commands.keySet());
        
        for (var value : commands.values()) {
            var command = (Map<String, Object>) value;
            parseAliases(command, results);
        }
    }
    
    protected void parseAliases(Map<String, Object> command, Map<String, Kind> results) {
        var value = command.get("aliases");
        if (value == null) {
            return;
        }
        
        if (value instanceof List<?>) {
            var aliases = (List<String>) value;
            
        } else {
            results.put("Invalid type for 'aliases', expected: 'List<String>, actual: '" +  value.getClass() + "'", ERROR);
        }
    }
    
}
