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
import com.karuslabs.commons.command.tree.nodes.*;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.*;

import java.lang.invoke.*;
import java.util.*;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Utility methods that modify {@code CommandNode}s.
 * <br><br>
 * <b>Implementation details: </b><br>
 * {@code VarHandle}s are used to manipulate the fields in a {@code CommandNode}.
 * An {@code ExceptionInInitializerError} will be thrown if strong module encapsulation 
 * is enabled.
 */
public @Static class Commands {
    
    private static final VarHandle COMMAND;
    private static final VarHandle CHILDREN;
    private static final VarHandle LITERALS;
    private static final VarHandle ARGUMENTS;
    
    static {
        try {
            var type = MethodHandles.privateLookupIn(CommandNode.class, MethodHandles.lookup());
            COMMAND = type.findVarHandle(CommandNode.class, "command", Command.class);
            CHILDREN = type.findVarHandle(CommandNode.class, "children", Map.class);
            LITERALS = type.findVarHandle(CommandNode.class, "literals", Map.class);
            ARGUMENTS = type.findVarHandle(CommandNode.class, "arguments", Map.class);
            
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Sets the {@code Command} which the given {@code CommandNode} is to execute.
     * 
     * @param <T> the type of the source
     * @param command the command
     * @param execution the {@code Command} that the {@code CommandNode} is to execute
     */
    public static <T> void execution(CommandNode<T> command, Command<T> execution) {
        COMMAND.set(command, execution);
    }
    
    /**
     * Removes the child from the given command. If the child is a {@code Aliasable},
     * the aliases of the child will also be removed.
     * 
     * @param <T> the type of the source
     * @param command the command which child is to be removed
     * @param child the name of the child to be removed
     * @return the child that was removed, or {@code null} if no child with the 
     *         given name exists
     */
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
    
}
