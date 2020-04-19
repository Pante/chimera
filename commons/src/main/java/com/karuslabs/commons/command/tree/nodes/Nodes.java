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
package com.karuslabs.commons.command.tree.nodes;

import com.karuslabs.annotations.Static;
import com.karuslabs.commons.command.*;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;

import java.util.function.Consumer;


public @Static class Nodes {

    public static abstract class Builder<T, B extends Builder<T, B>> extends ArgumentBuilder<T, B> {
    
        public B executes(Executable<T> command) {
            return executes((Command<T>) command);
        }


        public B optionally(ArgumentBuilder<T, ?> builder) {
            return optionally(builder.build());
        }

        public B optionally(CommandNode<T> node) {
            then(node);
            for (var child : node.getChildren()) {
                then(child);
            }

            return getThis();
        }

    }
    
    
    static <Node extends CommandNode<T> & Mutable<T>, T> void addChild(Node node, CommandNode<T> child, Consumer<CommandNode<T>> addition) {
        var current = node.getChild(child.getName());
        if (current != null) {
            node.removeChild(current.getName()); // Needs to be removed otherwise child will not replace current
            
            for (var grandchild : current.getChildren()) {
                child.addChild(grandchild); // Grandchildren need to be added since the current child was removed
            }
        }
        
        if (current instanceof Aliasable<?>) {
            for (var alias : ((Aliasable<T>) current).aliases()) {
                node.removeChild(alias.getName());
            }
        }
        
        addition.accept(child);
        if (child instanceof Aliasable<?>) {
            var aliases = ((Aliasable<T>) child).aliases();
            
            for (var alias : aliases) {
                addition.accept(alias);
            }
            
            if (current != null) {
                for (var alias : aliases) {
                    for (var grandchild : current.getChildren()) {
                        alias.addChild(grandchild);
                    }
                }
            }
        }
    }
    
}
