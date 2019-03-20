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

import java.util.List;


/**
 * A mutable {@code CommandNode} that defines several operation for convenience 
 * to modify the internal state of this {@code Node}.
 * 
 * @param <T> the type of the source
 */
public interface Node<T> {
    
    /**
     * Returns the aliases of this {@code Node}.
     * 
     * @return the aliases of this Node
     */
    public List<CommandNode<T>> aliases();

    
    /**
     * Adds the given child to this {@code Node} and the aliases of this {@code Node}. 
     * <br><br>
     * <b>Implementation requirement:</b><br>
     * The child must also be added to the aliases of this implementation.
     * 
     * @param child the child
     */
    public void addChild(CommandNode<T> child);
    
    /**
     * Removes the given child from this {@code Node} and the aliases of this {@code Node}.
     * <br><br>
     * <b>Implementation requirement:</b><br>
     * The child must also be removed from the aliases of this implementation.
     * 
     * @param child the child
     * @return the child that was removed; or null if this node contains no child
     *         with the given name
     */
    public CommandNode<T> removeChild(String child);
    
    
    /**
     * Returns the {@code Command} to be executed for this {@code Node}.
     * 
     * @return the command to be executed
     */
    public Command<T> getCommand();
    
    /**
     * Sets the {@code Command} to be executed for this {@code Node} and the aliases
     * of this {@code Node}.
     * <br><br>
     * <b>Implementation requirement:</b><br>
     * The child must also be set for the aliases of this implementation.
     * 
     * @param command the command to be executed
     */
    public void setCommand(Command<T> command);
    
    
    /**
     * Returns the {@code CommandNode} to which this {@code Node} is redirected.
     * 
     * @return the node to which this node is redirected
     */
    public CommandNode<T> getRedirect();
    
    /**
     * Sets the {@code CommandNode} to which this {@code Node} and the aliases of 
     * this {@code Node} are redirected.
     * <br><br>
     * <b>Implementation requirement:</b><br>
     * The destination of the redirection must also be set for the aliases of this 
     * implementation.
     * 
     * @param destination the node to which this node is redirected
     */
    public void setRedirect(CommandNode<T> destination);
    
}
