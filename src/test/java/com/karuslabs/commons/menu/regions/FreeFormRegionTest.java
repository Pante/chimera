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

import com.karuslabs.commons.core.test.StubServer;

import junitparams.*;

import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;


@RunWith(JUnitParamsRunner.class)
public class FreeFormRegionTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    
    private FreeFormRegion region;
    private Inventory inventory;
    
    
    public FreeFormRegionTest() {
        inventory = StubServer.INSTANCE.createInventory(region, InventoryType.HOPPER);
        region = new FreeFormRegion(inventory, 1);
    }
    
    
    @Test
    @Parameters({"1, true", "0, false"})
    public void within(int slot, boolean expected) {
        assertEquals(expected, region.within(slot));
    }
    
    
    @Test
    @Parameters({"-1", "10"})
    public void FreeFormRegion(int slot) {
        exception.expect(IndexOutOfBoundsException.class);
        new FreeFormRegion(inventory, slot);
    }
    
}
