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


public class Effect<Origin extends BoundLocation, Target extends BoundLocation> {    
    
    public static final int INFINITE = -1;
    
    private static final BiConsumer<Particles, Location> GLOBAL = Particles::render;
    
    private Plugin plugin;
    private Task<Task, Origin, Target> task;
    private boolean orientate;
    private long iterations;
    private long delay;
    private long period;
    boolean async;
    
    
    public Effect(Plugin plugin, Task<Task, Origin, Target> task, boolean orientate, long iterations, long delay, long period, boolean async) {
        this.plugin = plugin;
        this.task = task;
        this.orientate = orientate;
        this.iterations = iterations;
        this.delay = delay;
        this.period = period;
        this.async = async;
    }
    
        
    public Promise<?> render(Origin origin, Target target) {
        return schedule(new EffectTask<>(task.get(), GLOBAL, origin, target, orientate, iterations));
    }
    
    public Promise<?> render(Player player, Origin origin, Target target) {
        return schedule(new EffectTask<>(task.get(), (particles, location) -> particles.render(player, location), origin, target, orientate, iterations));
    }
    
    public Promise<?> render(Collection<Player> players, Origin origin, Target target) {
        Set<Player> targets = weakSet(players);
        return schedule(new EffectTask<>(task.get(), (particles, location) -> particles.render(targets, location), origin, target, orientate, iterations));
    }
    
    Promise<?> schedule(EffectTask<Origin, Target> task) {
        if (async) {
            task.runTaskTimerAsynchronously(plugin, delay, period);
            
        } else {
            task.runTaskTimer(plugin, delay, period);
        }
        
        return task;
    }
    
    
    public static<O extends BoundLocation, T extends BoundLocation> Builder<O, T> builder(Plugin plugin) {
        return new Builder<>(new Effect<>(plugin, null, false, 0, 0, 0, true));
    }
    
    public static class Builder<O extends BoundLocation, T extends BoundLocation> {

        private Effect<O, T> effect;

        Builder(Effect<O, T> effect) {
            this.effect = effect;
        }
        
        public Builder<O, T> task(Task<Task, O, T> task) {
            effect.task = task;
            return this;
        }
        public Builder<O, T> orientate(boolean orientate) {
            effect.orientate = orientate;
            return this;
        }
        
        public Builder<O, T> async(boolean async) {
            effect.async = async;
            return this;
        }
        
        public Builder<O, T> iterations(long iterations) {
            effect.iterations = iterations;
            return this;
        }
        
        public Builder<O, T> delay(long delay) {
            effect.delay = delay;
            return this;
        }
        
        public Builder<O, T> period(long period) {
            effect.period = period;
            return this;
        }
        
        public Effect<O, T> build() {
            return effect;
        }

    }
    
}
