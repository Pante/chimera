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

import org.bukkit.plugin.Plugin;


public abstract class AsynchronousEffect<P extends Particles, O extends BoundLocation, T extends BoundLocation> extends Effect<P, O, T> {
    
    public AsynchronousEffect(Plugin plugin, P particles, boolean orientate, long iterations, long delay, long period) {
        super(plugin, particles, orientate, iterations, delay, period);
    }

    
    @Override
    public Promise<?> schedule(Task task) {
        task.runTaskTimerAsynchronously(plugin, delay, period);
        return task;
    }
    
}
