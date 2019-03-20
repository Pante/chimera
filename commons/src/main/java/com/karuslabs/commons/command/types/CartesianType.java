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

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.*;

import java.util.concurrent.CompletableFuture;

import org.bukkit.*;
import org.bukkit.entity.Player;


/**
 * A Cartesian type.
 * 
 * @param <T> the type of the argument
 */
public abstract class CartesianType<T> implements Type<T> {    
    
    /**
     * Splits the string returned from the builder by whitespaces. The provision of 
     * suggestions is then delegated to {@link #suggest(SuggestionsBuilder, CommandContext, Location, String[])}
     * if the source is a {@code Player} and {@link #suggest(SuggestionsBuilder, CommandContext, String[])}.
     * 
     * @param <S> the type of the source
     * @param context the context
     * @param builder the builder
     * @return the suggestions
     */
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        var source = context.getSource();
        var parts = split(builder.getRemaining());
        
        if (source instanceof Player) {
            var block = ((Player) source).getTargetBlockExact(5);
            if (block != null) {
                suggest(builder, context, block.getLocation(), parts);
            }
        }
        
        suggest(builder, context, parts);
        
        return builder.buildFuture();
    }
    
    /**
     * Returns the string that was split by whitespaces.
     * 
     * @param remaining the string to be split
     * @return the parts of the string that was split by whitespace
     */
    protected String[] split(String remaining) {
        return remaining.split(" ");
    }
    
    
    
    /**
     * Provides suggestions using the given builder, context, location and argument
     * parts.
     * <br><br>
     * <b>Default implementation:</b><br>
     * Does nothing.
     * 
     * @param builder the builder
     * @param context the context
     * @param location the location of the block the source is looking at within
     *                 a 5 block radius
     * @param parts the parts of the argument split by a whitespace
     */
    protected void suggest(SuggestionsBuilder builder, CommandContext<?> context, Location location, String[] parts) {
        
    }
    
    /**
     * Provides suggestions using the given builder, context and argument parts.
     * <br><br>
     * <b>Default implementation:</b><br>
     * Does nothing.
     * 
     * @param builder the builder
     * @param context the context
     * @param parts the parts of the argument split by a whitespace
     */
    protected void suggest(SuggestionsBuilder builder, CommandContext<?> context, String[] parts) {
        
    }
    
}
