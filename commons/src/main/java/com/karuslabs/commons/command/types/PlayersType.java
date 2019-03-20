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

import com.karuslabs.commons.command.Lexer;

import com.mojang.brigadier.*;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.*;
import com.mojang.brigadier.suggestion.*;

import java.util.*;
import java.util.concurrent.*;

import org.bukkit.*;
import org.bukkit.entity.Player;


/**
 * A {@code Player} type that supports a double quotation mark enclosed comma separated 
 * list of player names and the following tags:
 * <ul>
 * <li>{@code @a} - All online players</li>
 * <li>{@code @r} - A random online player</li>
 * </ul>
 */
public class PlayersType implements StringType<List<Player>> {
    
    private static final SimpleCommandExceptionType INVALID = new SimpleCommandExceptionType(new LiteralMessage("'@a' cannot be used in a list of players."));
    private static final DynamicCommandExceptionType UNKNOWN = new DynamicCommandExceptionType(name -> new LiteralMessage("Unknown player or selector: " + name));
    private static final Collection<String> EXAMPLES = List.of("@a", "@r", "\"Pante, Kevaasaurus\"");
    static final Message ALL = new LiteralMessage("All online players");
    static final Message RANDOM = new LiteralMessage("A online player chosen at random");
    
    
    private Server server;
    
    
    /**
     * Constructs a {@code Player} type.
     */
    public PlayersType() {
        this.server = Bukkit.getServer();
    }
    
    
    /**
     * Returns the players whose names are contained in the string returned by the
     * given {@code StringReader} or were chosen through the supported tags.
     * 
     * @param reader the reader
     * @return the players whose names are contained in the given string or were
     *         chosen through the supported tags
     * @throws CommandSyntaxException if an unknown tag or player name was given
     * @throws CommandSyntaxException if @a was used in a comma separated list of
     *                                arguments
     */
    @Override
    public List<Player> parse(StringReader reader) throws CommandSyntaxException {
        var argument = reader.peek() == '"' ? reader.readQuotedString() : Lexer.until(reader, ' ');
        
        if (argument.equalsIgnoreCase("@a")) {
            return online();
        }
        
        var online = online();
        var players = new ArrayList<Player>();
        var names = Lexer.COMMA.split(argument);
        for (var name : names) {
            if (name.equalsIgnoreCase("@r")) {
                players.add(online.get(ThreadLocalRandom.current().nextInt(online.size())));
                continue;
            }
            
            var player = server.getPlayerExact(name);
            if (player != null) {
                players.add(player);
                
            } else if (name.equalsIgnoreCase("@a")) {
                throw INVALID.createWithContext(reader);
                
            } else {
                throw UNKNOWN.createWithContext(reader, name);
            }
        }
        
        
        return players;
    }
    
    List<Player> online() {
        // Interally, Bukkit uses a CopyWriteList, but we check anyways in case
        // some fucking idiot decides to create a fork of Bukkit that doesn't.
        if (server.getOnlinePlayers() instanceof List<?>) {
            return (List<Player>) server.getOnlinePlayers();

        } else {
            return new ArrayList<>(server.getOnlinePlayers());
        }
    }
    
    
    /**
     * Returns the player names and tags that begin with the given input.
     * 
     * @param <S> the type of the source
     * @param context the context
     * @param builder the builder
     * @return the player names and tags that begin with the remaining input
     */
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        var remaining = builder.getRemaining();
        if ("@".startsWith(remaining)) {
            builder.suggest("@a", ALL);
        }
        
        var source = context.getSource() instanceof Player ? (Player) context.getSource() : null;
        var enclosed = remaining.startsWith("\"");
        remaining = remaining.replace("\"", "");
        
        var parts = Lexer.COMMA.split(remaining, -1);
        var last = parts[parts.length - 1];
        var beginning = remaining.substring(0, remaining.lastIndexOf(last));
        
        if ("@r".startsWith(last)) {
            var suggestion = beginning + "@r";
            if (enclosed) {
                suggestion = '"' + suggestion +'"';
            }
            
            builder.suggest(suggestion, RANDOM);
        }
        
        for (var player: server.getOnlinePlayers()) {
            if ((source == null || source.canSee(player)) && player.getName().startsWith(last)) {
                var suggestion = beginning + player.getName();
                if (enclosed) {
                    suggestion = '"' + suggestion + '"';
                }
                
                builder.suggest(suggestion);
            }
        }        
        
        return builder.buildFuture();
    }
    
    
    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
    
}
