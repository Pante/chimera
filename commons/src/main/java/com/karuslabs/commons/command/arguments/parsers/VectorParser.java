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
import com.karuslabs.commons.util.Vectors;

import com.mojang.brigadier.*;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.*;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;


public @Static class VectorParser {
    
    static final SimpleCommandExceptionType SOURCE = new SimpleCommandExceptionType(new LiteralMessage("Source must be a player"));
    static final SimpleCommandExceptionType MIXED = new SimpleCommandExceptionType(new LiteralMessage("Cannot mix world and local coordinates (everything must either use ^ or not)")); 
    static final DynamicCommandExceptionType WORLD = new DynamicCommandExceptionType(world -> new LiteralMessage("Unknown world:" + world));
        
    static final Location INVALID = new Location(null, Double.NaN, Double.NaN, Double.NaN);
    
    
    public static Location toLocation(CommandContext<?> context, StringReader reader) throws CommandSyntaxException {
        var cursor = reader.getCursor();
        
        var world = Bukkit.getWorld(reader.readUnquotedString());
        if (world == null) {
            reader.setCursor(cursor);
        }
        
        var vector = to3DVector(context, reader);
        return new Location(world, vector.getX(), vector.getY(), vector.getZ());
    }
    
    
    public static World toWorld(CommandContext<?> context, StringReader reader) throws CommandSyntaxException {
        var name = reader.readUnquotedString();
        var world = Bukkit.getWorld(name);
        
        if (world == null) {
            throw WORLD.createWithContext(reader, name);
        }
        
        return world;
    }
    
        
    public static Vector to2DVector(CommandContext<?> context, StringReader reader) throws CommandSyntaxException {
        return toVector(context, reader, false);
    }
    
    public static Vector to3DVector(CommandContext<?> context, StringReader reader) throws CommandSyntaxException {
        return toVector(context, reader, true);
    }
    
    static Vector toVector(CommandContext<?> context, StringReader reader, boolean y) throws CommandSyntaxException {
        var location = of(context.getSource());
        
        if (reader.peek() == '^') {
            if (location == INVALID) {
                throw MIXED.createWithContext(reader);
            }
            
            var vector = new Vector(parseRelative(reader), y ? parseRelative(reader) : 0, parseRelative(reader));
            return Vectors.rotate(vector, location);
            
        } else {
            return new Vector(parseAbsolute(reader, location.getX()), y ? parseAbsolute(reader, location.getY()) : 0, parseAbsolute(reader, location.getZ()));
        }
    }
    
    
    static Location of(Object source) {
        if (source instanceof Player) {
            return ((Player) source).getLocation();
            
        } else {
            return INVALID;
        }
    }
    
    static double parseAbsolute(StringReader reader, double position) throws CommandSyntaxException {
        reader.skipWhitespace();
        switch (reader.peek()) {
            case '^':
                throw MIXED.createWithContext(reader);
                
            case '~':
                if (!Double.isNaN(position)) {
                    return position + reader.readDouble();
                    
                } else {
                    throw SOURCE.createWithContext(reader);
                }
                
            default:
                return reader.readDouble();
        }
    }
    
    static double parseRelative(StringReader reader) throws CommandSyntaxException {
        reader.skipWhitespace();
        if (reader.peek() == '^') {
            reader.skip();
            return reader.readDouble();
            
        } else {
            throw MIXED.createWithContext(reader);
        }
    }
    
}
