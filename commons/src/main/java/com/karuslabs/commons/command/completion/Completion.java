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

import java.util.List;
import javax.annotation.Nonnull;

import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static java.util.Collections.EMPTY_LIST;
import java.util.function.Predicate;
import static java.util.stream.Collectors.toList;


@FunctionalInterface
public interface Completion {
    
    public @Nonnull List<String> complete(@Nonnull CommandSender sender, @Nonnull String argument);
    
    
    public static final Completion NONE = (sender, argument) -> EMPTY_LIST;
    
    public static final Completion PLAYER_NAMES = (sender, argument) -> {
        Predicate<Player> predicate;
        if (sender instanceof Player) {
            Player source = (Player) sender;
            predicate = player -> source.canSee(player) && player.getName().startsWith(argument);
            
        } else {
            predicate = player -> player.getName().startsWith(argument);
        }
        
        return sender.getServer().getOnlinePlayers().stream().filter(predicate).map(Player::getName).collect(toList());
    };
            
    
    public static final Completion WORLD_NAMES = (sender, argument) -> 
            sender.getServer().getWorlds().stream().map(World::getName).filter(name -> name.startsWith(argument)).collect(toList());
    
}
