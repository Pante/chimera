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
import com.karuslabs.Satisfactory.Logger;

import java.util.*;

import static com.karuslabs.Satisfactory.Texts.quote;

// Does not work
public class DuplicateCommandLint extends Lint {

    private final Map<Identity, Command> namespace = new HashMap<>();
    
    public DuplicateCommandLint(Logger logger) {
        super(logger);
    }

    @Override
    public void lint(Environment environment, Command command) {
        var existing = namespace.put(command.identity, command);
        if (existing != null) {
            logger.error(command.site, quote(existing.path()) + " conflicts with " + quote(command.path()));
        }
        
        for (var child : command.children.values()) {
            lint(environment, child);
        }
        
        namespace.remove(command.identity);
    }

}
