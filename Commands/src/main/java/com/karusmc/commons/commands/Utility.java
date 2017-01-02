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
package com.karusmc.commons.commands;

import java.util.Arrays;


public class Utility {
    
    public static String[] trim(String[] args) {
        if (args.length <= 1) {
            return new String[]{};

        } else {
            return Arrays.copyOfRange(args, 1, args.length - 1);
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
    
}
