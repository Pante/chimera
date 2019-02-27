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
package com.karuslabs.commons.command.arguments.parsers;

import com.karuslabs.annotations.Static;

import com.mojang.brigadier.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import org.checkerframework.checker.nullness.qual.Nullable;


public @Static class VectorParser {
    
    static final DynamicCommandExceptionType EXCEPTION = new DynamicCommandExceptionType(val -> new LiteralMessage("Source must be a player"));
    
    
    public static Location parseLocation() {
        
    }
    
    public static Vector parse2DVector() {
        
    }
    
    public static Vector parse3DVector() {
        
    }
    
    
    public static double parse(StringReader reader, double position, double offset) throws CommandSyntaxException {
        if (reader.peek() == '~') {
            reader.skip();
            return position + reader.readDouble();
            
        } else if (reader.peek() == '^') {
            
        }
    }
    
}
