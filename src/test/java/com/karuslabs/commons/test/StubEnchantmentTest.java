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
package com.karuslabs.commons.test;

import org.bukkit.enchantments.*;

import org.junit.Test;

import static org.junit.Assert.*;


public class StubEnchantmentTest {
    
    private StubEnchantment resource;
    
    
    public StubEnchantmentTest() {
        resource = StubEnchantment.INSTANCE;
    }
    
    
    @Test
    public void getById() {
        Enchantment enchantment = Enchantment.getById(0);
        assertEquals(Enchantment.PROTECTION_ENVIRONMENTAL, enchantment);
    }
    
    
    @Test
    public void getByName() {
        Enchantment enchantment = Enchantment.getByName("PROTECTION_ENVIRONMENTAL");
        assertEquals(Enchantment.PROTECTION_ENVIRONMENTAL, enchantment);
    }
    
}
