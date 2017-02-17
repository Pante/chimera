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
package com.karuslabs.commons.commands;

import java.util.*;


public class Utility {
    
    private static final String[] EMPTY = new String[] {};
    
    
    public static String[] trim(String[] args) {
        if (args.length <= 1) {
            return EMPTY;
            
        } else {
            return Arrays.copyOfRange(args, 1, args.length);
        }
    }
    
    
    public static String getArgumentOrDefault(String[] args, int index, String defaultArgument) {
        if (index < args.length) {
            return args[index];
    
        } else {
            return defaultArgument;
        }
    }
    
    
    public static int toInt(String argument) {
        if (argument.matches("\\d+")) {
            return Integer.parseInt(argument);
            
        } else {
            return 1;
        }
    }
    
    
    public static int getTotalPages(int totalEntries, int pageSize) {
        if (totalEntries > 0 && pageSize > 0) {
            return Math.max(1, (int) Math.ceil((double) totalEntries / pageSize));
            
        } else {
            return 0;
        }
    }
    
    
    public static int getFirstIndex(int totalEntries, int pageSize, int page) {
        int firstOnPage = (page * pageSize) - pageSize;
        
        return (firstOnPage < totalEntries) ? firstOnPage : totalEntries;
    }
    
    
    public static int getLastIndex(int totalEntries, int pageSize, int page) {
        int lastOnPage = (page * pageSize);
        
        return (lastOnPage < totalEntries) ? lastOnPage : totalEntries;
    }


    public static void flapMap(String name, Command command, Map<String, Command> commands) {
        if (command instanceof Marshall) {
            ((Marshall) command).getCommands().values().forEach(subcommand -> flapMap(name + " " + subcommand.getName(), subcommand, commands));
        }
        
        commands.put(name, command);
    }
    
}
