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

import com.karuslabs.commons.command.Read;

import com.mojang.brigadier.*;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.*;
import com.mojang.brigadier.suggestion.*;

import java.util.*;
import java.util.concurrent.*;

import org.bukkit.*;
import org.bukkit.entity.Player;

import org.checkerframework.checker.nullness.qual.Nullable;


public class PlayersType implements StringType<List<Player>> {
    
    private static final SimpleCommandExceptionType INVALID = new SimpleCommandExceptionType(new LiteralMessage("Cannot use @a selector in a list of players. "));
    private static final DynamicCommandExceptionType UNKNOWN = new DynamicCommandExceptionType(name -> new LiteralMessage("Unknown player or selector: " + name));
    private static final Collection<String> EXAMPLES = List.of("@a", "@r", "\"Pante, Kevaasaurus\"");
    private static final Message ALL = new LiteralMessage("All online players");
    private static final Message RANDOM = new LiteralMessage("A random online player");
    
    
    private Server server;
    
    
    public PlayersType() {
        this.server = Bukkit.getServer();
    }
    
    
    @Override
    public List<Player> parse(StringReader reader) throws CommandSyntaxException {
        var argument = reader.peek() == '"' ? reader.readQuotedString() : Read.until(reader, ' ');
        
        if (argument.equalsIgnoreCase("@a")) {
            return online();
        }
        
        var online = online();
        var players = new ArrayList<Player>();
        var names = Read.COMMA.split(argument);
        for (var name : names) {
            System.out.println("Name: " + name);
            if (name.equalsIgnoreCase("@r")) {
                players.add(online.get(ThreadLocalRandom.current().nextInt(online.size())));
                continue;
            }
            
            var player = server.getPlayerExact(name);
            if (player != null) {
                players.add(player);
                
            } else if (argument.equalsIgnoreCase("@a")) {
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
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        var remaining = builder.getRemaining();
        if (remaining.isEmpty() || remaining.equals("@")) {
            builder.suggest("@a", ALL);
        }
        
        var source = context.getSource() instanceof Player ? (Player) context.getSource() : null;
        var parts = Read.COMMA.split(remaining, -1);
        var last = parts[parts.length - 1].replace("\"", "");
        var beginning = remaining.substring(0, remaining.lastIndexOf(last));
        
        if ("@r".startsWith(last)) {
            builder.suggest(beginning + "@r", RANDOM);
        }
        
        for (var player: server.getOnlinePlayers()) {
            if ((source == null || source.canSee(player)) && player.getName().startsWith(last)) {
                builder.suggest(beginning + player.getName());
            }
        }        
        
        return suggest(builder, source); 
    }
    
    protected CompletableFuture<Suggestions> suggest(SuggestionsBuilder builder, @Nullable Player source) {
        
        
        return builder.buildFuture();
    }
    
    
    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
    
}
