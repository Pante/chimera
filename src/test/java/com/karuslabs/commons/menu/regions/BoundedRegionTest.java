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

import com.karuslabs.commons.test.StubServer;

import junitparams.*;

import org.bukkit.inventory.Inventory;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


@RunWith(JUnitParamsRunner.class)
public class BoundedRegionTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    
    private BoundedRegion region;
    private Inventory inventory;
    
    
    public BoundedRegionTest() {
        inventory = StubServer.INSTANCE.createInventory(null, 27);
        region = new BoundedRegion(inventory, 3, 22);
    }
    
    
    @Test
    @Parameters({"-1, -2", "-1, 0", "0, -1", "30, 30", "30, 0", "0, 30"})
    public void BoundedRegion_ThrowsException(int min, int max) {
        exception.expect(IndexOutOfBoundsException.class);
        
        new BoundedRegion(inventory, min, max);
    }
    
    
    @Test
    @Parameters({"3", "4", "12", "13", "21", "22"})
    public void within_Is(int slot) {
        assertTrue(region.within(slot));
    }
    
    
    @Test
    @Parameters({"2", "20", "23"})
    public void within_IsNot(int slot) {
        assertFalse(region.within(slot));
    }
    
}
