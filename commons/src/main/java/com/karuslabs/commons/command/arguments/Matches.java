/*
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
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
package com.karuslabs.commons.command.arguments;

import com.google.common.primitives.*;

import com.karuslabs.commons.annotation.Static;

import java.util.function.Predicate;

import org.bukkit.Bukkit;


/**
 * This class consist exclusively of stateless {@code Predicate}s for testing arguments.
 */
@Static
public class Matches {
    
    /**
     * A predicate which tests if the specified argument is empty. 
     * Returns {@code true} if the specified argument is empty; else {@code false}.
     */
    public static final Predicate<String> EMPTY = String::isEmpty;
    
    /**
     * A predicate which tests if the specified argument is a {@code boolean}.
     * Returns {@code true} if the specified argument is equal to either {@code "true"} or {@code "false}, case insensitive; else {@code false}.
     */
    public static final Predicate<String> BOOLEAN = argument -> argument.toLowerCase().matches("(true|false)");
    
    /**
     * A predicate which tests if the specified argument is an {@code int}.
     * Returns {@code} true if the specified argument is an {@code int}; else {@code false}.
     */
    public static final Predicate<String> INT = argument -> Ints.tryParse(argument) != null;
    
    /**
     * A predicate which tests if the specified argument is a {@code double}.
     * Returns {@code} true if the specified argument is an {@code double}; else {@code false}.
     */
    public static final Predicate<String> DOUBLE = argument -> Doubles.tryParse(argument) != null;
    
    /**
     * A predicate which tests if the specified argument is a {@code float}.
     * Returns {@code} true if the specified argument is an {@code float}; else {@code false}.
     */
    public static final Predicate<String> FLOAT = argument -> Floats.tryParse(argument) != null;
    
    /**
     * A predicate which tests if the specified argument is the name of an online {@code Player}.
     * Returns {@code} true if the specified argument is the name of an online {@code Player}; else {@code false}.
     */
    public static final Predicate<String> PLAYER = argument -> Bukkit.getPlayer(argument) != null;
    
    /**
     * A predicate which tests if the specified argument is the name of a loaded {@code World}.
     * Returns {@code} true if the specified argument is the name of a loaded {@code World}; else {@code false}.
     */
    public static final Predicate<String> WORLD = argument -> Bukkit.getWorld(argument) != null;
    
}
