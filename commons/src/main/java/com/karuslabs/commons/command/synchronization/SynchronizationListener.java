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

import java.util.*;

import org.bukkit.event.*;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;


/**
 * A {@code Listener} that listens for {@code PlayerCommandSendEvent}s to resynchronize
 * the server's internal dispatcher with a client. 
 * <br><br>
 * A single {@code SynchronizationListener} is shared among several plugins on a server 
 * to prevent redundant resynchronization. 
 * <br><br>
 * <b>Note:</b><br>
 * An issue in Minecraft's command mapping causes redirected commands to be incorrectly
 * mapped and sent to clients if the name of the redirected command is alphabetically 
 * before the name of the destination. Hence, commands are re-mapped and resent after 
 * a {@code PlayerCommanndSendEvent} is emitted.
 */
class SynchronizationListener implements Runnable, Listener {

    private Synchronizer synchronizer;
    private BukkitScheduler scheduler;
    private Plugin plugin;
    Set<PlayerCommandSendEvent> events;
    boolean running;
    
    
    /**
     * Creates a {@code SynchronizationListener​} with the given parameters.
     * 
     * @param synchronizer the owning {@code Synchronizer}
     * @param scheduler the scheduler
     * @param plugin the owning plugin
     */
    SynchronizationListener(Synchronizer synchronizer, BukkitScheduler scheduler, Plugin plugin) {
        this.synchronizer = synchronizer;
        this.scheduler = scheduler;
        this.plugin = plugin;
        events = new HashSet<>();
        running = false;
    }
    
    
    /**
     * Synchronizes the internal dispatcher with the players in the queued {@code PlayerCommandSendEvent}s.
     */
    @Override
    public void run() {
        for (var event : events) {
            synchronizer.synchronize(event.getPlayer(), event.getCommands());
        }
        
        events.clear();
        running = false;
    }
    
    
    @EventHandler
    void synchronize(PlayerCommandSendEvent event) {
        if (event instanceof SynchronizationEvent) {
            return;
        }
        
        if (events.add(event) && !running) {
            scheduler.scheduleSyncDelayedTask(plugin, this);
            running = true;
        }
    }

}
