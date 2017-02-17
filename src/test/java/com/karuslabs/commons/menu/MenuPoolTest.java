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
package com.karuslabs.commons.menu;

import com.karuslabs.commons.menu.Menu;
import com.karuslabs.commons.menu.MenuPool;
import junitparams.*;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.*;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class MenuPoolTest {
    
    private static HumanEntity humanEntity = mock(HumanEntity.class);;
    
    private MenuPool pool;
    private Menu menu;
    
    
    public MenuPoolTest() {
        pool = new MenuPool();
        menu = mock(Menu.class);
    }
    
    
    @Before
    public void setup() {
        pool.getMenus().clear();
    }
    
    
    @Test
    @Parameters
    public void onClick(HumanEntity entity, int times) {
        pool.getMenus().put(entity, menu);
        
        InventoryClickEvent event = mock(InventoryClickEvent.class);
        when(event.getWhoClicked()).thenReturn(humanEntity);
        pool.onClick(event);
        
        verify(menu, times(times)).onClick(event);
    }
    
    protected Object[] parametersForOnClick() {
        return new Object[] {
            new Object[] {humanEntity, 1},
            new Object[] {mock(HumanEntity.class), 0}
        };
    }
    
    
    @Test
    @Parameters
    public void onDrag(HumanEntity entity, int times) {
        pool.getMenus().put(entity, menu);
        
        InventoryDragEvent event = mock(InventoryDragEvent.class);
        when(event.getWhoClicked()).thenReturn(humanEntity);
        pool.onDrag(event);
        
        verify(menu, times(times)).onDrag(event);
    }
    
    protected Object[] parametersForOnDrag() {
        return new Object[] {
            new Object[] {humanEntity, 1},
            new Object[] {mock(HumanEntity.class), 0}
        };
    }
    
    
}
