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
package com.karuslabs.commons.command.types.parsers;

import com.karuslabs.annotations.Static;
import com.karuslabs.commons.util.Position;

import com.mojang.brigadier.*;
import com.mojang.brigadier.exceptions.*;

import org.bukkit.*;
import org.bukkit.util.Vector;


public @Static class VectorParser {
    
    static final SimpleCommandExceptionType MIXED = new SimpleCommandExceptionType(new LiteralMessage("Cannot mix world and local coordinates (everything must either use ^ or not)")); 
    static final DynamicCommandExceptionType WORLD = new DynamicCommandExceptionType(world -> new LiteralMessage("Unknown world:" + world));
    
    
    public static World parseWorld(StringReader reader) throws CommandSyntaxException {
        var name = reader.readString();
        var world = Bukkit.getWorld(name);
        
        if (world == null) {
            throw WORLD.createWithContext(reader, name);
        }
        
        return world;
    }
    
    
    public static Position parse2DPosition(StringReader reader) throws CommandSyntaxException {
        return parsePosition(reader, false);
    }
    
    public static Position parse3DPosition(StringReader reader) throws CommandSyntaxException {
        return parsePosition(reader, true);
    }
    
    
    static Position parsePosition(StringReader reader, boolean y) throws CommandSyntaxException {
        var position = new Position();
        
        if (reader.peek() == '^') {
            position.rotate(true);
        }
        
        parse(reader, position, Position.X);
        if (y) {
            parse(reader, position, Position.Y);
        }
        parse(reader, position, Position.Z);
        
        return position;
    }
    
    static Position parse(StringReader reader, Position position, int axis) throws CommandSyntaxException {
        reader.skipWhitespace();
        
        if (position.rotate()) {
            if (reader.peek() != '^') {
                throw MIXED.createWithContext(reader);
            }
            
            reader.skip();
            return position.set(axis, reader.readDouble());

        } else {
            switch (reader.peek()) {
                case '^':
                    throw MIXED.createWithContext(reader);

                case '~':
                    position.relative(axis, true);
                    reader.skip(); // fallthrough

                default:
                    return position.set(axis, reader.readDouble());
            }
        }
    }
    
    
    public static Vector parse2DVector(StringReader reader) throws CommandSyntaxException {
        return parseVector(reader, false);
    }
    
    public static Vector parse3DVector(StringReader reader) throws CommandSyntaxException {
        return parseVector(reader, true);
    }
    
    
    static Vector parseVector(StringReader reader, boolean y) throws CommandSyntaxException {
        var vector = new Vector();
        
        reader.skipWhitespace();
        vector.setX(reader.readDouble());
        
        if (y) {
            reader.skipWhitespace();
            vector.setY(reader.readDouble());
        }
        
        reader.skipWhitespace();
        vector.setZ(reader.readDouble());
        
        return vector;
    }
    
}
