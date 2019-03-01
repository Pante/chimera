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
package com.karuslabs.commons.command.arguments;

import com.karuslabs.commons.command.arguments.parsers.VectorParser;
import com.karuslabs.commons.util.Position;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.*;
import java.util.regex.Pattern;

import org.bukkit.Location;


public class WorldPositionArgument extends SelectorArgument<Position> {
    
    static final Collection<String> EXAMPLES = List.of("world_name 0 0 0", "\"world name\" 0.0 0.0 0.0", "world_name ^ ^ ^", "world_name ~ ~ ~");
    static final Pattern PATTERN = Pattern.compile("\"?( |$)(?=(([^\"]*\"){2})*[^\"]*$)\"?");
    
    
    @Override
    public Position parse(StringReader reader) throws CommandSyntaxException {
        return VectorParser.parseWorldPosition(reader);
    }
    
    
    @Override
    protected String[] split(String remaining) {
        return PATTERN.split(remaining);
    }
    
    
    @Override
    protected void suggest(SuggestionsBuilder builder, CommandContext<?> context, Location location, String[] parts) {
        if (builder.getRemaining().isEmpty()) {
            var world = "\"" + location.getWorld().getName() + "\"";
            builder.suggest(world + location.getX());
            builder.suggest(world + location.getX() + " " + location.getY());
            builder.suggest(world + location.getX() + " " + location.getY() + " " + location.getZ());
            return;
        }
        
        switch (parts.length) {
            case 1:
                builder.suggest(parts[0] + location.getX());
                builder.suggest(parts[0] + location.getX() + " " + location.getY());
                builder.suggest(parts[0] + location.getX() + " " + location.getY() + " " + location.getZ());
                break;
                
            case 2:
                builder.suggest(parts[0] + " " + parts[1] + " " + location.getY());
                builder.suggest(parts[0] + " " + parts[1] + " " + location.getY() + " " + location.getZ());
                break;
                
            case 3:
                builder.suggest(parts[0] + " " + parts[1] + parts[2] + " " + location.getZ());
                break;
        }
    }
    
    @Override
    protected void suggest(SuggestionsBuilder builder, CommandContext<?> context, String[] parts) {
        if (builder.remaining.isEmpty()) {
            builder.suggest("world ~");
            builder.suggest("world ~ ~");
            builder.suggest("world ~ ~ ~");
            return;
        }
        
        var prefix = '~';
        switch (parts.length) {
            case 1:
                builder.suggest(parts[0] + " " + prefix);
                builder.suggest(parts[0] + " " + prefix + " " + prefix);
                builder.suggest(parts[0] + " " + prefix + " " + prefix + " " + prefix);
                break;
                
            case 2:
                prefix = parts[1].charAt(0) == '^' ? '^' : '~';
                builder.suggest(parts[0] + " " + parts[1] + " " + prefix);
                builder.suggest(parts[0] + " " + parts[1] + " " + prefix + " " + prefix);
                break;
                
            case 3:
                prefix = parts[1].charAt(0) == '^' ? '^' : '~';
                builder.suggest(parts[0] + " " + parts[1] + " " + parts[2] + " " + prefix);
                break;
        }
    }
    
    
    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
    
}
