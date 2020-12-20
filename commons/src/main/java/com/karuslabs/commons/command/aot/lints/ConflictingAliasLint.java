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
package com.karuslabs.commons.command.aot.lints;

import com.karuslabs.commons.command.aot.*;
import com.karuslabs.commons.command.aot.Identity.Type;

import com.karuslabs.puff.Logger;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import static com.karuslabs.commons.command.aot.lints.ConflictingAliasLint.Kind.*;
import static com.karuslabs.puff.Texts.quote;

public class ConflictingAliasLint extends Lint {
    
    enum Kind { ALIAS, NAME; }
    
    private final Map<String, Entry<Command, Kind>> names = new HashMap<>();
    
    public ConflictingAliasLint(Logger logger) {
        super(logger);
    }

    @Override
    public void lint(Environment environment, Command command) {
        for (var child : command.children.values()) {
            if (child.identity.type == Type.ARGUMENT) {
                continue;
            }
            
            lint(child, child.identity.name, NAME);
            
            for (var alias : child.aliases) {
                lint(child, alias, ALIAS);
            }
        }
        
        names.clear();
    }
    
    void lint(Command command, String name, Kind kind) {
        var existing = names.put(name, new SimpleEntry<>(command, kind));
        if (existing == null) {
            return;
        }
        
        if (kind == ALIAS && existing.getValue() == ALIAS) {
            logger.error(command.site, "Alias: " + quote(name) + " in " + quote(command.path()) + " conflicts with alias in " + quote(existing.getKey().path()));
            
        } else if (kind == ALIAS && existing.getValue() == NAME) {
            logger.error(command.site, "Alias: " + quote(name) + " in " + quote(command.path()) + " conflicts with " + quote(existing.getKey().path()));
            
        } else if (kind == NAME && existing.getValue() == ALIAS ){
            logger.error(existing.getKey().site, "Alias: " + quote(name) + " in " + quote(existing.getKey().path()) + " conflicts with " + quote(command.path()));
        }
    }

}
