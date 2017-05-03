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

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Utility {
    
    private static final String[] EMPTY = new String[0];
    
    
    private Utility() {}
    
    
    @SafeVarargs
    public static String[] trim(String... args) {
        if (args.length <= 1) {
            return EMPTY;
            
        } else {
            return Arrays.copyOfRange(args, 1, args.length);
        }
    }
    
    
    @SafeVarargs
    public static boolean within(int min, int max, String... args) {
        return min <= args.length && args.length <= max;
    }
    
    public static boolean isPermittedPlayer(CommandSender sender, Command command) {
        return sender instanceof Player && sender.hasPermission(command.getPermission());
    }
    
}
