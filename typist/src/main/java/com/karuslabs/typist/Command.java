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
package com.karuslabs.typist;

import com.karuslabs.typist.Binding.Pattern;

import java.util.*;
import javax.lang.model.element.*;

import org.checkerframework.checker.nullness.qual.Nullable;

public final class Command {
    
    public final @Nullable Command parent;
    public final Identity identity;
    public final TypeElement site;
    public final Set<String> aliases = new HashSet<>();
    public final Map<Element, Binding<?>> bindings = new HashMap<>();
    public final Map<Pattern.Group, Binding<?>> groups = new HashMap<>();
    public final Map<Identity, Command> children = new HashMap<>();

    public Command(Command parent, Identity identity, TypeElement site) {
        this.parent = parent;
        this.identity = identity;
        this.site = site;
    }
    
    public @Nullable Binding<?> binding(Pattern pattern) {
        for (var binding : bindings.values()) {
            if (binding.pattern == pattern) {
                return binding;
            }
        }
        
        return null;
    }
    
    public void bind(Binding<?> binding) {
        bindings.put(binding.site, binding);
        groups.put(binding.pattern.group, binding);
    }
        
    public String path() {
        if (parent == null) {
            return identity.toString();
        }
        
        var builder = new StringBuilder();
        var command = this;
        while (command != null) {
            builder.insert(0, ' ').insert(0, command.identity);
            command = command.parent;
        }

        return builder.deleteCharAt(builder.length() - 1).toString();
    }

}