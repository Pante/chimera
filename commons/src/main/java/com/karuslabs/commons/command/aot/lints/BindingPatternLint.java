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
import com.karuslabs.commons.command.aot.Binding.Pattern;
import com.karuslabs.puff.Logger;

import java.util.*;

import static com.karuslabs.puff.Texts.quote;

public class BindingPatternLint extends Lint {

    public BindingPatternLint(Logger logger) {
        super(logger);
    }

    @Override
    public void lint(Environment environment, Command command) {
        var bindings = new EnumMap<Pattern.Group, Binding<?>>(Pattern.Group.class);
        var logged = EnumSet.noneOf(Pattern.Group.class);
        
        for (var binding : command.bindings.values()) {
            var existing = bindings.put(binding.pattern.group, binding);
            if (existing == null) {
                continue;
            }
            
            var message = binding.pattern.article + " " + binding.pattern.type + " is already bound to " + quote(command.path());
            
            logger.error(binding.site, message);
            if (logged.add(binding.pattern.group)) {
                logger.error(existing.site, message);
            } 
        }
    }

}
