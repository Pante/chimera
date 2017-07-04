/* 
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.karuslabs.commons.menu.regions;

import com.karuslabs.commons.menu.buttons.Button;

import java.util.Collections;

import junitparams.*;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class BoxRegionTest {
    
    private BoxRegion region;
    private Player player;
    private Button button;
    
    
    public BoxRegionTest() {
        region = new BoxRegion(5, 12, 18);
        player = mock(Player.class);
        button = mock(Button.class);
        
        region.getButtons().put(13, button);
    }
    
    
    @Before
    public void setup() {
        region.setMin(12);
        region.setMax(18);
    }
    
    
    @Test
    @Parameters({"12, true", "18, true", "17, true", "7, false", "11, false", "19, false"})
    public void contains(int slot, boolean contains) {
        assertEquals(contains, region.contains(slot));
    }
    
    @Test
    @Parameters({"12, true", "18, true", "17, true", "7, false", "11, false", "19, false"})
    public void contains_inverted(int slot, boolean contains) {
        region.setMin(18);
        region.setMax(12);
        
        assertEquals(contains, region.contains(slot));
    }
    
    
    @Test
    @Parameters({"13, true, 1", "12, true, 0", "9, true, 0", "13, false, 0", "0, false, 0"})
    public void click(int slot, boolean hasPermission, int times) {
        InventoryClickEvent event = when(mock(InventoryClickEvent.class).getWhoClicked()).thenReturn(player).getMock();
        when(event.getRawSlot()).thenReturn(slot);
        when(player.hasPermission("")).thenReturn(hasPermission);
        
        region.click(null, event);
        
        verify(button, times(times)).click(null, event);
    }
    
    
    @Test
    @Parameters({"13, true, 1", "12, true, 0", "9, true, 0", "13, false, 0", "0, false, 0"})
    public void drag(int slot, boolean hasPermission, int times) {
        InventoryDragEvent event = when(mock(InventoryDragEvent.class).getWhoClicked()).thenReturn(player).getMock();
        when(event.getRawSlots()).thenReturn(Collections.singleton(slot));
        when(player.hasPermission("")).thenReturn(hasPermission);
        
        region.drag(null, event);
        
        verify(button, times(times)).drag(null, event);
    }
    
    
    @Test
    public void newBoxRegion() {
        BoxRegion region = BoxRegion.newBoxRegion().min(10).max(12).inventoryWidth(5).defaultButton(Button.NONE).build();
        
        assertEquals(10, region.getMin());
        assertEquals(12, region.getMax());
        assertEquals(5, region.width);
        assertEquals(true, Button.NONE == region.defaultButton);
    }
    
}
