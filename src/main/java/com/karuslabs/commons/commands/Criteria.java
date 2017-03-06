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

import org.bukkit.command.*;
import org.bukkit.entity.Player;


/**
 * Represents a criteria which must be satisfied for execution to proceed.
 */
@FunctionalInterface
public interface Criteria {
    
    /**
     * Criteria which always returns <code>true</code>
     */
    public static final Criteria NONE = (command, sender, args) -> true;
    
    /**
     * Criteria which returns <code>true</code>, if the <code>sender</code> has permission; else <code>false</code>
     */
    public static final Criteria PERMITTED = (command, sender, args) -> command.testPermissionSilent(sender);
    
    /**
     * Criteria which returns <code>true</code>, if the <code>sender</code> has permission and is a <code>Player</code>; else <code>false</code>
     */
    public static final Criteria PERMITTED_PLAYER = (command, sender, args) -> sender instanceof Player && command.testPermissionSilent(sender);
    
    /**
     * Criteria which returns <code>true</code>, if there are no arguments; else <code>false</code>
     */
    public static final Criteria NO_ARGUMENTS = (command, sender, args) -> args.length == 0;
  
    
    /**
     * Asserts whether the criteria is satisfied.
     * 
     * @param command the command
     * @param sender source object which is executing this command
     * @param args all of the command's arguments
     * @return true, if the criteria is satisfied; else false
     */
    public boolean test(Command command, CommandSender sender, String[] args);
    
    
    /**
     * Asserts if the array size is within the bounds specified.
     * 
     * @param args all of the command's arguments
     * @param min minimum array size, inclusive
     * @param max maximum array size, inclusive
     * @return true, if within bounds; else false
     */
    public static boolean within(String[] args, int min, int max) {
        return min <= args.length && args.length <= max;
    }
    
}
