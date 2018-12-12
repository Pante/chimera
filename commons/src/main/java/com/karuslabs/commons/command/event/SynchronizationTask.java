/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
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
package com.karuslabs.commons.command.event;

import com.karuslabs.commons.command.Dispatcher;
    
import java.util.*;

import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import org.checkerframework.checker.nullness.qual.Nullable;

    
public class SynchronizationTask implements Runnable {
    
    static final Set<PlayerCommandSendEvent> EVENTS = new HashSet<>();
    
    
    private BukkitScheduler scheduler;
    private Plugin plugin;
    private @Nullable Dispatcher dispatcher;
    private boolean scheduled;

    
    public SynchronizationTask(BukkitScheduler scheduler, Plugin plugin, @Nullable Dispatcher dispatcher) {
        this.scheduler = scheduler;
        this.plugin = plugin;
        this.dispatcher = dispatcher;
        this.scheduled = false;
    }
    
    
    public void add(PlayerCommandSendEvent event) {
        if (EVENTS.add(event) && !scheduled) {
            scheduler.scheduleSyncDelayedTask(plugin, this, 1L);
            scheduled = true;
        }
    }

    
    @Override
    public void run() {
        for (var event : EVENTS) {
            dispatcher.synchronize(event.getPlayer(), event.getCommands());
        }
        EVENTS.clear();
        this.scheduled = false;
    }
    
    
    public void set(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

}
