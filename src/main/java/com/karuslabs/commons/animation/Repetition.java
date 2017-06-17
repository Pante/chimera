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

import com.karuslabs.commons.util.TriFunction;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.*;


public enum Repetition {
    
    ONCE((scheduler, plugin, animation) -> {
        if (animation.isAsync()) {
            return scheduler.runTaskLaterAsynchronously(plugin, animation, animation.getDelay());
            
        } else {
            return scheduler.runTaskLater(plugin, animation, animation.getDelay());
        }
    }), 
    
    // Irony at its finest
    REPEATING((scheduler, plugin, animation) -> {
        if (animation.isAsync()) {
            return scheduler.runTaskTimerAsynchronously(plugin, animation, animation.getDelay(), animation.getPeriod());
            
        } else {
            return scheduler.runTaskTimer(plugin, animation, animation.getDelay(), animation.getPeriod());
        }
    }),
    
    INFINITE((scheduler, plugin, animation) -> {
        if (animation.isAsync()) {
            return scheduler.runTaskTimerAsynchronously(plugin, animation, animation.getDelay(), animation.getPeriod());
            
        } else {
            return scheduler.runTaskTimer(plugin, animation, animation.getDelay(), animation.getPeriod());
        }
    });
    
    
    private final TriFunction<BukkitScheduler, Plugin, Animation, BukkitTask> stratergy;
    
    private Repetition(TriFunction<BukkitScheduler, Plugin, Animation, BukkitTask> stratergy) {
        this.stratergy = stratergy;
    }


    public BukkitTask schedule(BukkitScheduler scheduler, Plugin plugin, Animation animation) {
        return stratergy.apply(scheduler, plugin, animation);
    }
    
}
