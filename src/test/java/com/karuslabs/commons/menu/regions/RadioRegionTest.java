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

import com.karuslabs.commons.menu.buttons.RadioButton;

import junitparams.*;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class RadioRegionTest {
    
    private RadioButton button;
    private RadioButton oldButton;
    private Player player;
    
    
    public RadioRegionTest() {
        button = mock(RadioButton.class);
        oldButton = mock(RadioButton.class);
        player = mock(Player.class);
    }
    
    
    @Test
    @Parameters({"1, true, true, 1, true", "0, true, true, 0, true", "1, false, true, 0, false", "1, true, false, 0, false"})
    public void click(int slot, boolean hasPermission, boolean select, int unselectTimes, boolean updated) {
        RadioRegion region = new RadioRegion(0, false);
        region.getButtons().put(slot, button);
        region.getButtons().put(0, oldButton);
        
        when(player.hasPermission("")).thenReturn(hasPermission);
        InventoryClickEvent event = when(mock(InventoryClickEvent.class).getWhoClicked()).thenReturn(player).getMock();
        when(event.getRawSlot()).thenReturn(slot);
        
        when(button.select(null, event)).thenReturn(select);
        
        region.click(null, event);
        
        verify(oldButton, times(unselectTimes)).unselect(null, event);
        assertEquals(updated, region.getSelectedSlot() == slot);
    }
    
    
    @Test
    @Parameters({"true, 1", "false, 0"})
    public void close(boolean reset, int times) {
        RadioRegion region = new RadioRegion(0, reset);
        region.getButtons().put(0, button);
        
        InventoryCloseEvent event = mock(InventoryCloseEvent.class);
        
        region.close(null, event);
        
        verify(button, times(times)).close(null, event, 0);
        assertEquals(reset, region.resets());
    }
    
    
    @Test
    public void newRadioRegion() {
        RadioRegion region = RadioRegion.newRadionRegion().selected(6).reset(true).build();
        assertEquals(6, region.getSelectedSlot());
        assertEquals(true, region.resets());
    }
    
}
