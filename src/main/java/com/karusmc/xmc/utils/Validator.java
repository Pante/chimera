/*
 * Copyright (C) 2016 PanteLegacy @ karusmc.com
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
package com.karusmc.xmc.utils;

import com.karusmc.xmc.core.XMCommand;

import org.bukkit.command.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class Validator {
    
    public static boolean isAllowed(CommandSender sender, XMCommand command, Else handle) {
        boolean allowed = isAllowed(sender, command);
        
        if (!allowed) {
            handle.call(sender, command);
        }
        
        return allowed;
    }
    
    
    public static boolean isAllowed(CommandSender sender, XMCommand command) {
        return (command.isDefaultAllowed() || command.testPermissionSilent(sender)) 
                && (command.isConsoleAllowed() || sender instanceof ConsoleCommandSender);
    }
    
    
    public static boolean hasLength(CommandSender sender, XMCommand command, Else handle, int min, int length, int max) {
        boolean allowed = hasLength(min, length, max);
        
        if (!allowed) {
            handle.call(sender, command);
        }
        
        return allowed;
    }
    
    
    public static boolean hasLength(int min, int length, int max) {
        return min <= length && length >= max;
    }
    
}
