/*
 * Copyright (C) 2017 Karus Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.karuslabs.commons.commands;

import java.util.*;
import java.util.stream.*;

import org.apache.commons.lang.StringUtils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


/**
 * 
 */
public interface Argument {
    
    public static final Argument PLAYER_NAMES = (sender, command, argument) -> {
        Stream<? extends Player> stream = sender.getServer().getOnlinePlayers().stream();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            stream = stream.filter(p -> player.canSee(p));
        }
        
        return stream.filter(p -> StringUtils.startsWithIgnoreCase(p.getName(), argument)).map(Player::getName).collect(Collectors.toList());
    };
    
    public static final Argument NONE = (sender, command, argument) -> Collections.EMPTY_LIST;
    
    
    public List<String> complete(CommandSender sender, Command command, String argument);
    
}
