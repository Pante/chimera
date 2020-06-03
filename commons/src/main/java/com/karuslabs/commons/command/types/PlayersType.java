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

import com.karuslabs.commons.command.Readers;

import com.mojang.brigadier.*;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.*;
import com.mojang.brigadier.suggestion.*;

import java.util.*;
import java.util.concurrent.*;

import org.bukkit.*;
import org.bukkit.entity.Player;


public class PlayersType implements StringType<List<Player>> {
    
    private static final SimpleCommandExceptionType INVALID = new SimpleCommandExceptionType(new LiteralMessage("'@a' cannot be used in a list of players."));
    private static final DynamicCommandExceptionType UNKNOWN = new DynamicCommandExceptionType(name -> new LiteralMessage("Unknown player or selector: " + name));
    private static final List<String> EXAMPLES = List.of("@a", "@r", "\"Pante, Kevaasaurus\"");
    static final Message ALL = new LiteralMessage("All online players");
    static final Message RANDOM = new LiteralMessage("A online player chosen at random");
    
    
    private Server server;
    
    
    public PlayersType() {
        this.server = Bukkit.getServer();
    }
    
    
    @Override
    public List<Player> parse(StringReader reader) throws CommandSyntaxException {
        var argument = reader.peek() == '"' ? reader.readQuotedString() : Readers.until(reader, ' ');
        
        if (argument.equalsIgnoreCase("@a")) {
            return online();
        }
        
        var online = online();
        var players = new ArrayList<Player>();
        var names = Readers.COMMA.split(argument);
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
    
    
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(S source, CommandContext<S> context, SuggestionsBuilder builder) {
        var remaining = builder.getRemaining();
        if ("@".startsWith(remaining)) {
            builder.suggest("@a", ALL);
        }
        
        var sender = source instanceof Player ? (Player) source : null;
        var enclosed = remaining.startsWith("\"");
        remaining = remaining.replace("\"", "");
        
        var parts = Readers.COMMA.split(remaining, -1);
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
            if ((sender == null || sender.canSee(player)) && player.getName().startsWith(last)) {
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
    public List<String> getExamples() {
        return EXAMPLES;
    }
    
}
