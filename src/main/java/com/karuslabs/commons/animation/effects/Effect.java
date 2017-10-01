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
package com.karuslabs.commons.animation.effects;

import com.karuslabs.commons.animation.particles.Particles;
import com.karuslabs.commons.util.concurrent.Promise;
import com.karuslabs.commons.world.BoundLocation;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiConsumer;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static com.karuslabs.commons.collection.Sets.weakSet;


public abstract class Effect<Particle extends Particles, Origin extends BoundLocation, Target extends BoundLocation> {
    
    private static final BiConsumer<Particles, Location> GLOBAL = Particles::render;
    
    protected Plugin plugin;
    protected Particle particles;
    protected boolean orientate;
    protected long iterations;
    protected long delay;
    protected long period;
    protected TimeUnit unit;
    
    
    public Effect(Plugin plugin, Particle particles, boolean orientate, long iterations, long delay, long period, TimeUnit unit) {
        this.plugin = plugin;
        this.particles = particles;
        this.orientate = orientate;
        this.iterations = iterations;
        this.delay = delay;
        this.period = period;
        this.unit = unit;
    }
    
    
    public Promise<?> render(Player player, Origin origin, Target target) {
        return schedule(task((particles, location) -> particles.render(player, location), origin, target));
    }
    
    public Promise<?> render(Collection<Player> players, Origin origin, Target target) {
        Set<Player> targets = weakSet(players);
        return schedule(task((particles, location) -> particles.render(targets, location), origin, target));
    }
    
    public Promise<?> render(Origin origin, Target target) {
        return schedule(task(GLOBAL, origin, target));
    }
    
    
    protected abstract Task task(BiConsumer<Particles, Location> render, Origin origin, Target target);
    
    protected abstract Promise<?> schedule(Task task);
    
}
