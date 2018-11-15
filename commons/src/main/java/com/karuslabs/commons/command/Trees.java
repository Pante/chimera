/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
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
package com.karuslabs.commons.command;

import com.mojang.brigadier.tree.*;

import java.util.*;
import java.util.function.Predicate;

import org.checkerframework.checker.nullness.qual.Nullable;


public class Trees {
    
    private static final Predicate<?> TRUE = source -> true;
    
    
    public static <S, T> CommandNode<T> map(CommandNode<S> command) {
        return map(command, null, new IdentityHashMap<>());
    }
    
    public static <S, T> @Nullable CommandNode<T> map(CommandNode<S> command, S source) {
        return map(command, source, new IdentityHashMap<>());
    }
    
    public static <S, T> @Nullable CommandNode<T> map(CommandNode<S> command, @Nullable S source, Map<CommandNode<S>, CommandNode<T>> mapped) {       
        if (source != null && !command.canUse(source)) {
            return null;
        }
        
        CommandNode<T> target;
        
        if (command.getRedirect() == null) {
            target = reinterpret(command);
            for (var child : command.getChildren()) {
                var kid = find(child, source, mapped);
                if (kid != null) {
                    target.addChild(kid);
                }
            }
            
        } else {
            var destination = find(command.getRedirect(), source, mapped);
            target = reinterpret(command, destination);
        }
        
        return target;
    }
    
    private static <S, T> @Nullable CommandNode<T> find(CommandNode<S> command, @Nullable S source, Map<CommandNode<S>, CommandNode<T>> mapped) {
        var target = mapped.get(command);
        if (target == null) {
            target = map(command, source, mapped);
            mapped.put(command, target);
        }
        
        return target;
    }
    
    
    public static <T> CommandNode<T> reinterpret(CommandNode<?> command) {
        return reinterpret(command, null);
    }
    
    public static <T> CommandNode<T> reinterpret(CommandNode<?> command, @Nullable CommandNode<T> destination) {
        if (command instanceof ArgumentCommandNode) {
            var type = ((ArgumentCommandNode<?, ?>) command).getType();
            return new ArgumentCommandNode<>(command.getName(), type, null, (Predicate<T>) TRUE, destination, null, false, null);
            
        } else if (command instanceof LiteralCommandNode) {
            return new LiteralCommandNode<>(command.getName(), null, (Predicate<T>) TRUE, destination, null, false);
            
        } else if (command instanceof RootCommandNode) {
            return new RootCommandNode<>();
            
        } else {
            throw new UnsupportedOperationException("Unsupported node \"" + command.getName() + "\" of type: " + command.getClass().getName());
        }
    }
    
}
