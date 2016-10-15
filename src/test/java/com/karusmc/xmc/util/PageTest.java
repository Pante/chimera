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

import junitparams.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */

@RunWith(JUnitParamsRunner.class)
public class PageTest {
    
    @Test
    @Parameters({"3, 3", "invalid, 1", "3.01, 1", "-3, 1"})
    public void getPageNumber_ReturnsNumber(String argument, int expected) {
        int returned = Page.getPageNumber(argument);
        assertEquals(expected, returned);
    }
    
    
    @Test
    @Parameters({"7, 3, 3", "2, 3, 1", "0 , 0, 0", "0, 3, 0", "3, 0, 0"})
    public void getTotalPages_ReturnsNumber(int divisibleSize, int pageSize, int expected) {
        int returned = Page.getTotalPages(divisibleSize, pageSize);
        assertEquals(expected, returned);
    }
    
    
    @Test
    @Parameters({"3, 4, 9"})
    public void getFirstIndex_ReturnsIndex(int page, int pageSize, int expected) {
        int returned = Page.getFirstIndex(page, pageSize);
        assertEquals(expected, returned);
    }
    
    
    @Test
    @Parameters({"1, 2, 3, 2", "1, 2, 1, 1"})
    public void getLastIndex_ReturnsIndex(int page, int pageSize, int divisibleSize, int expected) {
        int returned = Page.getLastIndex(page, pageSize, divisibleSize);
        assertEquals(expected, returned);
    }
    
}
