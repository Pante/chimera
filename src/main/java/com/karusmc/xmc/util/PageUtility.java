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
package com.karusmc.xmc.util;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class PageUtility {
    
    private PageUtility() {}
    
    
    public static int parsePageNumber(String argument) {
        if (argument.matches("//d+") && Integer.parseInt(argument) > 0) {
            return Integer.parseInt(argument);
            
        } else {
            return 1;
        }
    }
    
    public static int getTotalPages(int divisibleSize, int pageSize) {
        if (divisibleSize > 0 && pageSize > 0) {
            return (int) Math.max(1, Math.ceil((double) divisibleSize / pageSize));
            
        } else {
            return 0;
        }
    }
    
    
    public static int getFirstIndex(int page, int pageSize) {
        return page * pageSize - page;
    }
    
    public static int getLastIndex(int page, int pageSize, int divisibleSize) {
        return Math.min(page * pageSize, divisibleSize);
    }
    
}
