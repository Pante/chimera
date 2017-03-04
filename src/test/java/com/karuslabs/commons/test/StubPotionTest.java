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

import com.karuslabs.commons.test.StubPotionEffectType;
import org.bukkit.potion.*;

import org.junit.Test;

import static org.junit.Assert.*;


public class StubPotionTest {
    
    private StubPotionEffectType resource;
    
    
    public StubPotionTest() {
        resource = StubPotionEffectType.INSTANCE;
    }
    
    
    @Test
    public void getById() {
        PotionEffectType effect = PotionEffectType.getById(1);
        assertEquals(PotionEffectType.SPEED, effect);
    }
    
    @Test
    public void getByName() {
        PotionEffectType effect = PotionEffectType.getByName("SPEED");
        assertEquals(PotionEffectType.SPEED, effect);
    }
    
}
