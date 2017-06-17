/*
 * Copyright (C) 2017 Karus Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.karuslabs.commons.animation;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.*;


public class AnimationScheduler {
    
    private BukkitScheduler scheduler;
    private Plugin plugin;
    private ConcurrentMap<Animation, BukkitTask> animations;
    
    private AtomicBoolean shutdown;
    
    
    public AnimationScheduler(BukkitScheduler scheduler, Plugin plugin) {
        this.scheduler = scheduler;
        this.plugin = plugin;
        animations = new ConcurrentHashMap<>();
    }
    
    
    public void submit(Animation animation) {
        if (shutdown.get()) {
            throw new IllegalStateException("");
        }
        
        animations.compute(animation, (anAnimation, task) -> {
            if (task == null) {
                return anAnimation.getRepetition().schedule(scheduler, plugin, anAnimation);
            }
            else {
                throw new IllegalArgumentException("");
            }
        });
    }
    
    
    public void shutdown() {
        if (shutdown.compareAndSet(false, true)) {
            cancel();
            
        } else {
            throw new IllegalStateException();
        }
    }
    
    
    public void cancel() {
        animations.keySet().forEach(this::cancel);
    }

    public void cancel(Animation animation) {
        BukkitTask task = animations.remove(animation);
        if (task != null) {
            task.cancel();
            scheduler.runTask(plugin, animation.getCallback());
        }
    }
    
    
    public boolean isShutdown() {
        return shutdown.get();
    }
    
}
