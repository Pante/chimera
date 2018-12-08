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
    
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import org.checkerframework.checker.nullness.qual.Nullable;

    
public class SynchronizationTask implements Runnable {
    
    private BukkitScheduler scheduler;
    private Plugin plugin;
    private @Nullable Dispatcher dispatcher;
    private Queue<Player> players;
    private boolean scheduled;

    
    public SynchronizationTask(BukkitScheduler scheduler, Plugin plugin) {
        this(scheduler, plugin, null, new ConcurrentLinkedQueue<>());
}
    
    public SynchronizationTask(BukkitScheduler scheduler, Plugin plugin, @Nullable Dispatcher dispatcher, Queue<Player> players) {
        this.scheduler = scheduler;
        this.plugin = plugin;
        this.dispatcher = dispatcher;
        this.players = players;
        this.scheduled = false;
    }
    
    
    public void add(Player player) {
        players.offer(player);
        if (!scheduled) {
            scheduler.scheduleSyncDelayedTask(plugin, this);
        }
    }

    
    @Override
    public void run() {
        var player = players.poll();
        
        while (player != null) {
            dispatcher.synchronize(player);
            player = players.poll();
        }
        
        this.scheduled = false;
    }
    
    
    public void set(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

}
