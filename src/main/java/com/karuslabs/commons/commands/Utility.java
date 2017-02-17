/*
 * Copyright (C) 2016 Karus Labs
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


/**
 * Provides classes with utility methods.
 */
public class Utility {
    
    private static final String[] EMPTY = new String[] {};
    
    
    /**
     * Trims the specified array and removes the first value.
     * 
     * @param args The arguments to trim
     * @return Returns the trimmed array or an empty array if the arrays length was less than or equal to 1
     */
    public static String[] trim(String[] args) {
        if (args.length <= 1) {
            return EMPTY;
            
        } else {
            return Arrays.copyOfRange(args, 1, args.length);
        }
    }
    
    
    /**
     * Gets the value at the specified index or the specified default argument if the specified index
     * was out of bounds.
     * 
     * @param args The arguments containing the value
     * @param index The index of the value
     * @param defaultArgument The default argument to return if the index was out of bounds
     * @return The value at the specified index or the default argument if the index was out of bounds.
     */
    public static String getArgumentOrDefault(String[] args, int index, String defaultArgument) {
        if (index < args.length) {
            return args[index];
    
        } else {
            return defaultArgument;
        }
    }
    
    
    /**
     * Parses the specified string to an integer.
     * 
     * @param argument The argument to parse
     * @return The parsed value or 1 if the specified string was not a integer
     */
    public static int toInt(String argument) {
        if (argument.matches("\\d+")) {
            return Integer.parseInt(argument);
            
        } else {
            return 1;
        }
    }
    
    
    /**
     * Gets the total number of pages based on the specified entries and page size.
     * 
     * @param totalEntries The total entries
     * @param pageSize The number of entries on each page
     * @return The number of pages or 0 if the total entries or page size was less than 0
     */
    public static int getTotalPages(int totalEntries, int pageSize) {
        if (totalEntries > 0 && pageSize > 0) {
            return Math.max(1, (int) Math.ceil((double) totalEntries / pageSize));
            
        } else {
            return 0;
        }
    }
    
    
    /**
     * Gets the index of the first entry on a page based on the specified entries, page size and page.
     * 
     * @param totalEntries The total entries
     * @param pageSize The number of entries on each page
     * @param page The page
     * @return The index of the first entry on a page or the total number of entries if the index was out of bounds.
     */
    public static int getFirstIndex(int totalEntries, int pageSize, int page) {
        int firstOnPage = (page * pageSize) - pageSize;
        
        return (firstOnPage < totalEntries) ? firstOnPage : totalEntries;
    }
    
    
    /**
     * Gets the index of the last entry o a page based on the specified entries, page size and page.
     * 
     * @param totalEntries The total entries
     * @param pageSize The number of entries on each page
     * @param page The page
     * @return The index of the last entry on a page or the total number of entries if the index was out of bounds.
     */
    public static int getLastIndex(int totalEntries, int pageSize, int page) {
        int lastOnPage = (page * pageSize);
        
        return (lastOnPage < totalEntries) ? lastOnPage : totalEntries;
    }

    
    /**
     * Converts the tree structured commands into a flat format.
     * 
     * For example, if a command with the name, "command" contains a subcommand with the name "subcommand".
     * Using this method on the command will add both to the map as "command" and "command subcommand" respectively.
     * 
     * @param name The name of the command
     * @param command The command to be mapped
     * @param commands The map the commands are to be mapped to
     */
    public static void flapMap(String name, Command command, Map<String, Command> commands) {
        if (command instanceof Marshall) {
            ((Marshall) command).getCommands().values().forEach(subcommand -> flapMap(name + " " + subcommand.getName(), subcommand, commands));
        }
        
        commands.put(name, command);
    }
    
}
