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

import org.bukkit.event.*;
import org.bukkit.event.server.ServiceUnregisterEvent;
import org.bukkit.plugin.*;
import org.bukkit.scheduler.BukkitScheduler;


public class PulseListener implements Listener {
    
    Synchronizer synchronizer;
    Plugin plugin;
    BukkitScheduler scheduler;
    ServicesManager services;
    
    
    public PulseListener(Synchronizer synchronizer, Plugin plugin) {
        this.synchronizer = synchronizer;
        this.plugin = plugin;
        this.scheduler = plugin.getServer().getScheduler();
        this.services = plugin.getServer().getServicesManager();
    }
    
    
    @EventHandler
    protected void listen(ServiceUnregisterEvent event) {
        if (event.getProvider().getService() == SynchronizationListener.class && !services.isProvidedFor(SynchronizationListener.class)) {
            var listener = new SynchronizationListener(synchronizer, scheduler, plugin);
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
            services.register(SynchronizationListener.class, listener, plugin, ServicePriority.Normal);
        }
    }
    
}
