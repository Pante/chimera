/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
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
package com.karuslabs.commons.command.synchronization;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandSendEvent;

import org.bukkit.scheduler.BukkitScheduler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SynchronizationTest {
    
    Synchronizer synchronizer = mock(Synchronizer.class);
    BukkitScheduler scheduler = mock(BukkitScheduler.class);
    Synchronization synchronization = new Synchronization(synchronizer, scheduler, null);
    PlayerCommandSendEvent event = mock(PlayerCommandSendEvent.class);
    
    
    @Test
    void add() {
        synchronization.add(event);
        
        assertTrue(synchronization.events.contains(event));
        assertTrue(synchronization.running);
        verify(scheduler).scheduleSyncDelayedTask(null, synchronization);
    }
    
    
    @Test
    void add_duplicate() {
        synchronization.events.add(event);
        synchronization.add(event);
        
        assertTrue(synchronization.events.contains(event));
        assertFalse(synchronization.running);
        verify(scheduler, times(0)).scheduleSyncDelayedTask(null, synchronization);
    }
    
    
    @Test
    void add_running() {
        synchronization.running = true;
        synchronization.add(event);
        
        assertTrue(synchronization.events.contains(event));
        assertTrue(synchronization.running);
        verify(scheduler, times(0)).scheduleSyncDelayedTask(null, synchronization);
    }
    
    
    @Test
    void run() {
        when(event.getPlayer()).thenReturn(mock(Player.class));
        when(event.getCommands()).thenReturn(List.of("a"));
        
        synchronization.add(event);
        synchronization.run();
        
        verify(synchronizer).synchronize(any(Player.class), any(List.class));
        assertTrue(synchronization.events.isEmpty());
        assertFalse(synchronization.running);
    }

} 
