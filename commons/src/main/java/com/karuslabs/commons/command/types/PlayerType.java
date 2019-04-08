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
import com.mojang.brigadier.exceptions.*;
import com.mojang.brigadier.suggestion.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import org.bukkit.*;
import org.bukkit.entity.Player;

import org.checkerframework.checker.nullness.qual.Nullable;


/**
 * A {@code Player} type.
 */
public class PlayerType implements WordType<Player> {
    
    private static final DynamicCommandExceptionType EXCEPTION = new DynamicCommandExceptionType(name -> new LiteralMessage("Unknown player: " + name));
    private static final Collection<String> EXAMPLES = List.of("Bob", "Pante");

    
    private Server server;
    
    
    /**
     *  Creates a {@code PlayerType}.
     */
    public PlayerType() {
        this.server = Bukkit.getServer();
    }
    
    
    /**
     * Returns a online player whose name matches the string returned by the given 
     * {@code StringReader}.
     * 
     * @param reader the reader
     * @return a player with the given name
     * @throws CommandSyntaxException if a player with the give name does not exist
     */
    @Override
    public Player parse(StringReader reader) throws CommandSyntaxException {
        var name = reader.readUnquotedString();
        var player = Bukkit.getPlayerExact(name);
        
        if (player == null) {
            throw EXCEPTION.createWithContext(reader, name);
        }
        
        return player;
    }

    
    /**
     * Returns the names of online players that start with the remaining input of 
     * the given {@code SuggesitonBuilder}. If the source is a player, a check is 
     * performed to determine the visibility of the suggested player to the source. 
     * Players that are invisible to the source are not suggested.
     * <br><br>
     * <b>Implementation details:</b><br>
     * Suggestion is forwarded to {@link #suggest(SuggestionsBuilder, Player)}.
     * 
     * @param <S> the type of the source
     * @param context the context
     * @param builder the builder
     * @return the player names that begin with the remaining input
     */
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        var source = context.getSource() instanceof Player ? (Player) context.getSource() : null;
        return suggest(builder, source); 
    }
    
    /**
     * Suggests online players whose names start with the remaining input of the given
     * {@code SuggestionBuilder} and if the source is player, are visible to the 
     * source.
     * 
     * @param builder the builder
     * @param source the source, or {@code null} if the source is not a player
     * @return the player names that begin with the remaining input
     */
    protected CompletableFuture<Suggestions> suggest(SuggestionsBuilder builder, @Nullable Player source) {
        for (var player: server.getOnlinePlayers()) {
            if ((source == null || source.canSee(player)) && player.getName().startsWith(builder.getRemaining())) {
                builder.suggest(player.getName());
            }
        }
        
        return builder.buildFuture();
    }
    

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
    
}
