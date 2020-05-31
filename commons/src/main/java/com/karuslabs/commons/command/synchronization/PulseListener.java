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


/**
 * A {@code Listener} that listens for a registered {@code SynchronizationListener}
 * service to be unregistered. When a {@code SynchronizationListener} is unregistered,
 * this {@code PulseListener} attempts to register a new {@code SynchronizationListener}
 * that is owned by this plugin to the {@code ServiceManager}. 
 * <br><br>
 * <b>Implementation details:</b><br>
 * If successful, all plugins that require a {@code SynchronizationListener} will 
 * share the one owned by this plugin. Otherwise, the {@code SynchronizationListener}
 * that was registered by another plugin before this {@code PulseLitener} will be
 * used instead.
 * 
 * The order in which new {@code SychronizationListener}s are registered is undefined.
 */
public class PulseListener implements Listener {
    
    Synchronizer synchronizer;
    Plugin plugin;
    BukkitScheduler scheduler;
    ServicesManager services;
    
    
    /**
     * Creates a {@code PulseListener}
     * 
     * @param synchronizer the synchronizer
     * @param plugin the owning plugin
     */
    public PulseListener(Synchronizer synchronizer, Plugin plugin) {
        this.synchronizer = synchronizer;
        this.plugin = plugin;
        this.scheduler = plugin.getServer().getScheduler();
        this.services = plugin.getServer().getServicesManager();
    }
    
    
    /**
     * Registers a {@code SynchronizationListener} owned by this plugin if no
     * {@code SynchronizationListener} has been registered yet. Otherwise, does
     * nothing.
     */
    public void register() {
        if (!services.isProvidedFor(SynchronizationListener.class)) {
            var listener = new SynchronizationListener(synchronizer, scheduler, plugin);
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
            services.register(SynchronizationListener.class, listener, plugin, ServicePriority.Normal);
        }
    }
    
    
    /**
     * Attempts to register a {@code SynchronizationListener} owned by this plugin
     * if a {@code SynchronizationListener} was unregistered.
     * 
     * @param event the event
     */
    @EventHandler
    void listen(ServiceUnregisterEvent event) {
        if (event.getProvider().getService() == SynchronizationListener.class) {
            register();
        }
    }
    
}
