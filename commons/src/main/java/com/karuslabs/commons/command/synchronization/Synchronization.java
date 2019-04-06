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

import java.util.*;

import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;


/**
 * A delayed synchronization task that synchronizes the internal dispatcher of the
 * server with players.
 */
public class Synchronization implements Runnable {
    
    private Synchronizer synchronizer;
    private BukkitScheduler scheduler;
    private Plugin plugin;
    Set<PlayerCommandSendEvent> events;
    boolean running;
    
    
    /**
     * Creates a {@code Synchronization​} with the given parameters.
     * 
     * @param synchronizer the owning {@code Synchronizer}
     * @param scheduler the scheduler
     * @param plugin the owning plugin
     */
    public Synchronization(Synchronizer synchronizer, BukkitScheduler scheduler, Plugin plugin) {
        this.synchronizer = synchronizer;
        this.scheduler = scheduler;
        this.plugin = plugin;
        this.events = new HashSet<>();
        this.running = false;
    }
    
    
    /**
     * Schedules a synchronization between the internal dispatcher and the player 
     * in the given {@code event} if not already scheduled.
     * 
     * @param event the event which denotes a synchronization between the internal
     *              dispatcher of the server and a client
     */
    public void add(PlayerCommandSendEvent event) {
        if (events.add(event) && !running) {
            scheduler.scheduleSyncDelayedTask(plugin, this);
            running = true;
        }
    }
    
    
    /**
     * Synchronizes the internal dispatcher with the players added via {@link #add(PlayerCommandSendEvent)}.
     */
    @Override
    public void run() {
        for (var event : events) {
            synchronizer.synchronize(event.getPlayer(), event.getCommands());
        }
        
        events.clear();
        running = false;
    }
    
}
