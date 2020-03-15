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

import com.karuslabs.annotations.Static;
import com.karuslabs.commons.command.annotations.assembler.Assembler;
import com.karuslabs.commons.command.tree.nodes.*;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.*;

import java.lang.invoke.*;
import java.util.*;

import org.checkerframework.checker.nullness.qual.Nullable;


public @Static class Commands {
    
    static final Assembler<?> ASSEMBLER = new Assembler<>();
    
    static final VarHandle COMMAND;
    static final VarHandle CHILDREN;
    static final VarHandle LITERALS;
    static final VarHandle ARGUMENTS;
    
    static {
        try {
            var targetClass = MethodHandles.privateLookupIn(CommandNode.class, MethodHandles.lookup());
            COMMAND = targetClass.findVarHandle(CommandNode.class, "command", Command.class);
            CHILDREN = targetClass.findVarHandle(CommandNode.class, "children", Map.class);
            LITERALS = targetClass.findVarHandle(CommandNode.class, "literals", Map.class);
            ARGUMENTS = targetClass.findVarHandle(CommandNode.class, "arguments", Map.class);
            
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    
    public static <T> void executes(CommandNode<T> command, Command<T> execution) {
        COMMAND.set(command, execution);
    }
    
        
    public static <T> @Nullable CommandNode<T> remove(CommandNode<T> command, String child) {
        var children = (Map<String, CommandNode<T>>) CHILDREN.get(command);
        var literals = (Map<String, ?>) LITERALS.get(command);
        var arguments = (Map<String, ?>) ARGUMENTS.get(command);

        
        var removed = children.remove(child);
        literals.remove(child);
        arguments.remove(child);
        
        if (removed instanceof Aliasable<?>) {
            for (var alias : ((Aliasable<?>) removed).aliases()) {
                var name = alias.getName();
                children.remove(name);
                literals.remove(name);
                arguments.remove(name);
            }
        }

        return removed;
    }
    
    
    public static <T> @Nullable CommandNode<T> resolve(Object annotated, String name) {
        return Commands.<T>resolve(annotated).get(name);
    }
    
    public static <T> Map<String, CommandNode<T>> resolve(Object annotated) {
        return (Map<String, CommandNode<T>>) ASSEMBLER.assemble(annotated);
    }
    
}
