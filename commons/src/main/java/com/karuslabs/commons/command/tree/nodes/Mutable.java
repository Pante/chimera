/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
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

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.CommandNode;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A mutable node that contains self-modification operations.
 * 
 * @param <T> the type of the source
 */
public interface Mutable<T> {
    
    /**
     * Adds the given child to this {@code Node} and its aliases. 
     * <br><br>
     * <b>Implementation requirement:</b><br>
     * The {@code child} must also be added to its aliases.
     * 
     * @param child the child
     */
    public void addChild(CommandNode<T> child);
    
    /**
     * Removes the given child from this {@code Node} and its aliases.
     * <br><br>
     * <b>Implementation requirement:</b><br>
     * The child must also be removed from its aliases.
     * 
     * @param child the child
     * @return the child that was removed if present; otherwise {@code null}
     */
    public @Nullable CommandNode<T> removeChild(String child);
    
    
    /**
     * Returns the {@code Command} to be executed.
     * 
     * @return the command to be executed
     */
    public Command<T> getCommand();
    
    /**
     * Sets the {@code command} as the command to be executed by this {@code Node}
     * and its aliases.
     * <br><br>
     * <b>Implementation requirement:</b><br>
     * The command must also be set for its aliases.
     * 
     * @param command the command to be executed
     */
    public void setCommand(Command<T> command);
    
    
    /**
     * Returns the destination of this node, or {@code null} if this node is not 
     * redirected.
     * 
     * @return the destination of this node, or {@code null} if this node is not 
     *         redirected
     */
    public @Nullable CommandNode<T> getRedirect();
    
    /**
     * Sets the destination of this node and its aliases when redirected.
     * <br><br>
     * <b>Implementation requirement:</b><br>
     * The destination must also be set for its aliases.
     * 
     * @param destination the node to which this node is redirected
     */
    public void setRedirect(CommandNode<T> destination);
    
}
