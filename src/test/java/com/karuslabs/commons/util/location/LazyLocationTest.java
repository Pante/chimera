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
package com.karuslabs.commons.util.location;

import com.karuslabs.commons.util.location.LazyLocation;
import junitparams.*;

import org.bukkit.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class LazyLocationTest {
    
    private LazyLocation lazy;
    
    
    public LazyLocationTest() {
        World world = mock(World.class);
        lazy = new LazyLocation("world", 0, 0, 0);
        lazy.location = mock(Location.class);
        lazy.server = when(mock(Server.class).getWorld("world")).thenReturn(world).getMock();
    }
    
    
    @Test
    public void load() {
        lazy.load();
        
        verify(lazy.location).setWorld(any(World.class));
    }
    
}
