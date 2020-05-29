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
package com.karuslabs.commons.command.types;

import com.mojang.brigadier.*;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import org.bukkit.*;


/**
 * A {@code World} type.
 */
public class WorldType implements StringType<World> {
    
    private static final DynamicCommandExceptionType WORLD = new DynamicCommandExceptionType(world -> new LiteralMessage("Unknown world: \"" + world + "\""));
    private static final List<String> EXAMPLES = List.of("my_fancy_world", "\"Yet another world\"");
    
    
    /**
     * Returns a world which name matches the string returned by the given {@code StringReader}.
     * A name that contains whitespaces must be enclosed in double quotation marks.
     * 
     * @see VectorParser#parseWorld(StringReader) 
     * 
     * @param reader the reader
     * @return a world with the given name
     * @throws CommandSyntaxException if a world with the given name does not exist
     */
    @Override
    public World parse(StringReader reader) throws CommandSyntaxException {
        var name = reader.readString();
        var world = Bukkit.getWorld(name);
        
        if (world == null) {
            throw WORLD.createWithContext(reader, name);
        }
        
        return world;
    }
    
    /**
     * Returns the worlds that start with the remaining input of the given {@code SuggesitonBuilder}.
     * 
     * @param <S> the type of the source
     * @param context the context
     * @param builder the builder
     * @return the world names that start with the remaining input
     */
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (var world : Bukkit.getWorlds()) {
            if (world.getName().startsWith(builder.getRemaining())) {
                builder.suggest(world.getName().contains(" ") ? '"' + world.getName() + '"' : world.getName());
            }
        }
        
        return builder.buildFuture();
    }


    @Override
    public List<String> getExamples() {
        return EXAMPLES;
    }
    
}
