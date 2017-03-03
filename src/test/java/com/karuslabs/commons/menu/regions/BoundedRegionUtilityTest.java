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
package com.karuslabs.commons.menu.regions;

import junitparams.*;

import org.bukkit.event.inventory.InventoryType;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.bukkit.event.inventory.InventoryType.*;
import static org.junit.Assert.assertEquals;


@RunWith(JUnitParamsRunner.class)
public class BoundedRegionUtilityTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    
    @Test
    @Parameters
    public void getLength(InventoryType type, int expected) {
        assertEquals(expected, BoundedRegionUtility.getLength(type));
    }
    
    protected Object[] parametersForGetLength() {
        return new Object[] {
            new Object[] {CRAFTING, 2},
            new Object[] {DISPENSER, 3},
            new Object[] {DROPPER, 3},
            new Object[] {CHEST, 9},
            new Object[] {ENDER_CHEST, 9}
        };
    }
    
    
    @Test
    public void getLength_ThrowsException() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Type: HOPPER is not supported");
        
        BoundedRegionUtility.getLength(HOPPER);
    }
    
}
