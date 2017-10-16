/*
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
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
package com.karuslabs.commons.animation.particles.effect;

import com.karuslabs.commons.animation.particles.Particles;
import com.karuslabs.commons.util.concurrent.Promise;
import com.karuslabs.commons.world.BoundLocation;

import java.util.*;
import java.util.function.*;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static com.karuslabs.commons.collection.Sets.weakSet;


public class Effect {    
    
    public static final int INFINITE = -1;
    
    private static final BiConsumer<Particles, Location> GLOBAL = Particles::render;
    
    private Plugin plugin;
    private Supplier<Task> supplier;
    private boolean orientate;
    private long iterations;
    private long delay;
    private long period;
    boolean async;
    
    
    public Effect(Plugin plugin, Supplier<Task> supplier, boolean orientate, long iterations, long delay, long period, boolean async) {
        this.plugin = plugin;
        this.supplier = supplier;
        this.orientate = orientate;
        this.iterations = iterations;
        this.delay = delay;
        this.period = period;
        this.async = async;
    }
    
        
    public Promise<?> render(BoundLocation origin, BoundLocation target) {
        return schedule(new EffectTask(supplier.get(), GLOBAL, origin, target, orientate, iterations));
    }
    
    public Promise<?> render(Player player, BoundLocation origin, BoundLocation target) {
        return schedule(new EffectTask(supplier.get(), (particles, location) -> particles.render(player, location), origin, target, orientate, iterations));
    }
    
    public Promise<?> render(Collection<Player> players, BoundLocation origin, BoundLocation target) {
        Set<Player> targets = weakSet(players);
        return schedule(new EffectTask(supplier.get(), (particles, location) -> particles.render(targets, location), origin, target, orientate, iterations));
    }
    
    Promise<?> schedule(EffectTask task) {
        if (async) {
            task.runTaskTimerAsynchronously(plugin, delay, period);
            
        } else {
            task.runTaskTimer(plugin, delay, period);
        }
        
        return task;
    }
    
    
    public static Builder builder(Plugin plugin) {
        return new Builder(new Effect(plugin, null, false, 0, 0, 0, true));
    }
    
    public static class Builder {

        private Effect effect;

        
        private Builder(Effect effect) {
            this.effect = effect;
        }
        
        public Builder supplier(Supplier<Task> supplier) {
            effect.supplier = supplier;
            return this;
        }
        public Builder orientate(boolean orientate) {
            effect.orientate = orientate;
            return this;
        }
        
        public Builder async(boolean async) {
            effect.async = async;
            return this;
        }
        
        public Builder iterations(long iterations) {
            effect.iterations = iterations;
            return this;
        }
        
        public Builder delay(long delay) {
            effect.delay = delay;
            return this;
        }
        
        public Builder period(long period) {
            effect.period = period;
            return this;
        }
        
        public Effect build() {
            return effect;
        }

    }
    
}
