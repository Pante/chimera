/*
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
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
package com.karuslabs.commons.command.completion;

import com.karuslabs.commons.command.argument.Arguments;
import com.karuslabs.commons.command.*;

import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static java.util.stream.Collectors.toList;


@FunctionalInterface
public interface Completer {
    
    public List<String> complete(CommandContext context, Arguments args);
    
    
    public static final Completer PLAYER_NAMES = (context, args) -> {
        Stream<? extends Player> players = context.getCommandSender().getServer().getOnlinePlayers().stream();
        String argument = args.getLastString();
        Player player = context.getPlayer();
        
        if (player != null) {
            players = players.filter(p -> player.canSee(p));
        }
        
        if (!argument.isEmpty()) {
            players = players.filter(p -> StringUtils.startsWithIgnoreCase(p.getName(), argument));
        }
        
        return players.map(Player::getName).collect(toList());
    };
    
    
    public static final Completer SUBCOMMANDS = (context, args) ->  {
        Stream<Command> commands = context.getCalleeCommand().getSubcommands().values().stream();
        String argument = args.getLastString();
        CommandSender sender = context.getCommandSender();
        
        if (!argument.isEmpty()) {
            commands.filter(command -> command.getName().startsWith(argument));
        }
        
        return commands.filter(command -> sender.hasPermission(command.getPermission())).map(Command::getName).collect(toList());
    };
    
    
    public static final Completer WORLD_NAMES = (context, args) ->  {
        Stream<World> worlds = context.getCommandSender().getServer().getWorlds().stream();
        String argument = args.getLastString();
        
        if (!argument.isEmpty()) {
            worlds.filter(world -> world.getName().startsWith(argument));
        }
        
        return worlds.map(World::getName).collect(toList());
    };
    
}
