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
package com.karuslabs.commons.command.aot;

import java.util.*;
import javax.lang.model.element.Element;

import org.checkerframework.checker.nullness.qual.Nullable;


public class Node {
    
    public static enum Type {
        ARGUMENT, LITERAL;
    }
    

    public final String name;
    public final Type type;
    public final Map<String, Element> aliases;
    public final Map<String, Node> children;
    public final Element element;
    public @Nullable Element argumentType;
    public @Nullable Element execution;
    public @Nullable Element suggestions;
    
    
    public static Node argument(String name, Element element) {
        return new Node(name, Type.ARGUMENT, element);
    }
    
    public static Node literal(String name, Element element) {
        return new Node(name, Type.LITERAL, element);
    }
    
    
    Node(String name, Type type, Element element) {
        this.name = name;
        this.type = type;
        this.aliases = new HashMap<>();
        this.children = new HashMap<>();
        this.element = element;
    }
    
    
    public Node addChild(Node child) {
        var entry = children.putIfAbsent(child.name, child);
        return entry == null ? child : entry;
    }
    
}
