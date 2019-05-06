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
package com.karuslabs.commons.command.annotations.assembler;

import com.mojang.brigadier.tree.CommandNode;

import java.util.*;

import org.checkerframework.checker.nullness.qual.Nullable;


/**
 * An intermediate representation of {@code CommandNode}s created from annotated
 * objects.
 * 
 * @param <T> the type of the source
 */
public class Node<T> {
    
    private String name;
    private @Nullable CommandNode<T> command;
    private Map<String, Node<T>> children;
    
    
    /**
     * Creates a {@code Node} with the given name and no {@code Command}.
     * 
     * @param name the name
     */
    public Node(String name) {
        this(name, null);
    }
    
    /**
     * Creates a {@code Node} with the given name and {@code Command}.
     * 
     * @param name the name
     * @param command the command to be executed
     */
    public Node(String name, @Nullable CommandNode<T> command) {
        this.name = name;
        this.command = command;
        children = new HashMap<>();
    }
    
    
    /**
     * Returns the name of this {@code Node}.
     * 
     * @return the name
     */
    public String name() {
        return name;
    }
    
    
    /**
     * Returns a {@code CommandNode} which this {@code Node} represents.
     * 
     * @return a {@code CommandNode}
     */
    public @Nullable CommandNode<T> get() {
        return command;
    }
    
    /**
     * Sets the {@code CommandNode} which this {@code Node} represents.
     * 
     * @param command the command to be executed
     */
    public void set(CommandNode<T> command) {
        this.command = command;
    }
    
    
    /**
     * Returns the children of this {@code Node}.
     * 
     * @return the children
     */
    public Map<String, Node<T>> children() {
        return children;
    }
    
}
