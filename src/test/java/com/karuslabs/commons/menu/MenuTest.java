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

import com.google.common.collect.Sets
        ;
import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Consumer;

import junitparams.*;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;

import org.junit.*;

import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class MenuTest {
    
    private Menu<Inventory> menu;
    private Inventory inventory;
    private Consumer<HumanEntity> closure;
    
    private Button defaultButton;
    private Button button;
    
    
    public MenuTest() {
        inventory = mock(Inventory.class);
        closure = mock(Consumer.class);
        
        defaultButton = mock(Button.class);
        button = mock(Button.class);
        
        menu = new Menu(inventory, defaultButton, closure);
        menu.bind(button, 0);
    }
    
    
    @Test
    @Parameters({"1, 0, 1", "0, 1, 0"})
    public void onClick(int slot, int bindedTimes, int defaultTimes) {
        InventoryClickEvent event = mock(InventoryClickEvent.class);
        when(event.getRawSlot()).thenReturn(slot);
        
        menu.onClick(event);
        
        verify(button, times(bindedTimes)).onClick(any(Menu.class), any(InventoryClickEvent.class));
        verify(defaultButton, times(defaultTimes)).onClick(any(Menu.class), any(InventoryClickEvent.class));
    }
    
    
    @Test
    @Parameters({"1, 0, 1", "0, 1, 0"})
    public void onDrag(int slot, int bindedTimes, int defaultTimes) {
        InventoryDragEvent event = mock(InventoryDragEvent.class);
        HashSet<Integer> set = Sets.newHashSet(slot);
        when(event.getRawSlots()).thenReturn(set);
        
        menu.onDrag(event);
        
        verify(button, times(bindedTimes)).onDrag(any(Menu.class), any(InventoryDragEvent.class));
        verify(defaultButton, times(defaultTimes)).onDrag(any(Menu.class), any(InventoryDragEvent.class));
    }
    
    
    @Test
    public void onClose() {
        Player player = mock(Player.class);
        InventoryCloseEvent event = mock(InventoryCloseEvent.class);
        
        when(event.getPlayer()).thenReturn(player);
        
        menu.onClose(event);
        
        verify(closure, times(1)).accept(player);
    }
    
    
    @Test
    public void bind_Item() {
        menu.bind(new ItemStack(Material.AIR), 1, 10);
        
        verify(inventory, times(2)).setItem(any(int.class), any(ItemStack.class));
    }
    
    
    @Test
    public void bind_Buttons() {
        menu.bind(button, 1, 10);
        
        assertTrue(menu.getButtons().keySet().containsAll(Arrays.asList(1, 10)));
    }
    
}
