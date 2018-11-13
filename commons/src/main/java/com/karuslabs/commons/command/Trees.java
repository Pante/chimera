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

import com.mojang.brigadier.builder.*;
import com.mojang.brigadier.tree.*;

import java.util.*;


public class Trees {
    
    public static <S, T> CommandNode<T> map(CommandNode<S> command) {
        return map(command, new HashMap<>());
    }
    
    public static <S, T> CommandNode<T> map(CommandNode<S> command, Map<CommandNode<S>, CommandNode<T>> mapped) {
        var target = Trees.<S, T>rebuild(command);
        
        var redirected = command.getRedirect();
        if (redirected != null) {
            var destination = mapped.get(redirected);
            if (destination == null) {
                destination = map(redirected, mapped);
                mapped.put(redirected, destination);
            }

            target.redirect(destination);
        }
        
        for (var child : command.getChildren()) {
            var next = mapped.get(child);
            if (next == null) {
                next = map(child, mapped);
                mapped.put(child, next);
            }
            
            target.then(next);
        }
        
        return target.build();
    }
    
    public static <S, T> ArgumentBuilder<T, ?> rebuild(CommandNode<S> command) {
        if (command instanceof ArgumentCommandNode) {
            var type = ((ArgumentCommandNode<S, ?>) command).getType();
            ArgumentBuilder<T, ?> builder = RequiredArgumentBuilder.argument(command.getName(), type);
            return builder;
            
        } else if (command instanceof LiteralCommandNode) {
            return LiteralArgumentBuilder.<T>literal(command.getName());
            
        } else {
            throw new UnsupportedOperationException("Unsupported node type: " + command.getClass().getName());
        }
    }
    
}
