/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SynchronizationListenerTest {
    
    Synchronizer synchronizer = mock(Synchronizer.class);
    BukkitScheduler scheduler = mock(BukkitScheduler.class);
    Plugin plugin = mock(Plugin.class);
    SynchronizationListener listener = new SynchronizationListener(synchronizer, scheduler, plugin);
    
    
    @Test
    void run() {
        var player = mock(Player.class);
        var event = new SynchronizationEvent(player, List.of("a"));
        
        listener.events.add(event);
        
        listener.running = true;
        
        listener.run();
        
        verify(synchronizer).synchronize(player, List.of("a"));
        assertTrue(listener.events.isEmpty());
        assertFalse(listener.running);
    }
    
    
    @Test
    void synchronize_event_ignore_event() {
        listener.synchronize(new SynchronizationEvent(null, List.of()));
        
        verifyNoInteractions(scheduler);
    }
    
    
    @Test
    void synchronize_event_new_task() {
        assertFalse(listener.running);
        
        listener.synchronize(mock(PlayerCommandSendEvent.class));
        
        verify(scheduler).scheduleSyncDelayedTask(plugin, listener);
        assertTrue(listener.running);
    }
    
    
    @Test
    void synchronize_event_existing_task() {
        listener.running = true;
        
        listener.synchronize(mock(PlayerCommandSendEvent.class));
        
        verifyNoInteractions(scheduler);
    }

} 
