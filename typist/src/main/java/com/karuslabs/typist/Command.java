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

/**
 * An intermediate representation of a command in a tree of commands,  i.e. {@code <player>} 
 * in {@code /tell <player>}.
 */
public record Command(@Nullable Command parent, Identity identity, TypeElement site, Set<String> aliases, Map<Element, Binding<?>> bindings, Map<Pattern.Group, Binding<?>> groups, Map<Identity, Command> children) {

    /**
     * Creates a command with the given arguments.
     * 
     * @param parent the parent of this command
     * @param identity this command's identity
     * @param site the declaration site of this command
     */
    public Command(@Nullable Command parent, Identity identity, TypeElement site) {
        this(parent, identity, site, new HashSet<>(), new HashMap<>(), new HashMap<>(), new HashMap<>());
    }
    
    /**
     * Returns a binding for the part of a command which the given pattern represents.
     * 
     * @param pattern the pattern
     * @return the binding, or {@code null} if no such binding exists
     */
    public @Nullable Binding<?> binding(Pattern pattern) {
        for (var binding : bindings.values()) {
            if (binding.pattern() == pattern) {
                return binding;
            }
        }
        
        return null;
    }
    
    /**
     * Binds the given bindings to this command.
     * 
     * @param binding a binding
     */
    public void bind(Binding<?> binding) {
        bindings.put(binding.site(), binding);
        groups.put(binding.pattern().group, binding);
    }
    
    /**
     * Returns a path from the root command to this command.
     * 
     * @return a path
     */
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
    
    
    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof Command command
            && identity.equals(command.identity)
            && site.equals(command.site)
            && aliases.equals(command.aliases)
            && children.equals(command.children);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(identity, site, aliases, children);
    }
    
    @Override
    public String toString() {
        return "{ identity: " + identity
             + ", site: " + site
             + ", aliases: " + aliases
             + ", children: " + children
             + "}";
    }

}