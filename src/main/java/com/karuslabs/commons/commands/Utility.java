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
 * Command-related utility methods.
 */
public class Utility {
    
    private static final String[] EMPTY = new String[0];
    
    
    /**
     * Removes the first value of the array specified.
     * 
     * @param args the array to trim
     * @return Returns a copy of the array with the first value removed if the array size is greater than 1; else an empty array.
     */
    public static String[] trim(String[] args) {
        if (args.length <= 1) {
            return EMPTY;
            
        } else {
            return Arrays.copyOfRange(args, 1, args.length);
        }
    }
    
    
    /**
     * Returns the value at the index, or the default value if the index specified is out of bounds.
     * 
     * @param args the array to retrieve the value from
     * @param index the index of the value
     * @param defaultArgument the value to return if the index was out of bounds
     * @return the value at the index, if within bounds; else the default value
     */
    public static String getArgumentOrDefault(String[] args, int index, String defaultArgument) {
        if (index < args.length) {
            return args[index];
    
        } else {
            return defaultArgument;
        }
    }
    
    
    /**
     * Parses the string to an <code>int</code>.
     * 
     * @param argument the string to parse
     * @return the parsed value if the string is a int; else 1
     */
    public static int toInt(String argument) {
        if (argument.matches("\\d+")) {
            return Integer.parseInt(argument);
            
        } else {
            return 1;
        }
    }
    
    
    /**
     * Returns the total number of pages based on the entries and page size specified.
     * 
     * @param totalEntries the total entries
     * @param pageSize the size of each page
     * @return the total number of pages, if the entries and size of a page is greater than 0; else 0
     */
    public static int getTotalPages(int totalEntries, int pageSize) {
        if (totalEntries > 0 && pageSize > 0) {
            return Math.max(1, (int) Math.ceil((double) totalEntries / pageSize));
            
        } else {
            return 0;
        }
    }
    
    
    /**
     * Returns the index of the first entry on the page specified.
     * 
     * @param totalEntries the total entries
     * @param pageSize the size of a page
     * @param page the page number
     * @return the index of the first entry on a page, if within the total entries; else the total number of entries
     */
    public static int getFirstIndex(int totalEntries, int pageSize, int page) {
        int firstOnPage = (page * pageSize) - pageSize;
        
        return (firstOnPage < totalEntries) ? firstOnPage : totalEntries;
    }
    
    
    /**
     * Returns the index of the last entry on the page specified.
     * 
     * @param totalEntries the total entries
     * @param pageSize the size of a page
     * @param page the page number
     * @return the index of the last entry on a page, if within the total entries; else the total number of entries
     */
    public static int getLastIndex(int totalEntries, int pageSize, int page) {
        int lastOnPage = (page * pageSize);
        
        return (lastOnPage < totalEntries) ? lastOnPage : totalEntries;
    }

    
    /**
     * Recursively traverses the <code>Command</code> and maps the subcommands to the <code>Map</code> specified by 
     * appending the name of the subcommand to the parent command.
     * 
     * @param name the name of the command
     * @param command the command to be operated on
     * @param commands the map the subcommands will be mapped to
     */
    public static void flapMap(String name, Command command, Map<String, Command> commands) {
        if (command instanceof Marshall) {
            ((Marshall) command).getSubcommands().values().forEach(subcommand -> flapMap(name + " " + subcommand.getName(), subcommand, commands));
        }
        
        commands.put(name, command);
    }
    
}
