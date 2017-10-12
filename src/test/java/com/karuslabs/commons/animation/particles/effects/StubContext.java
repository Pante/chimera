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
package com.karuslabs.commons.animation.particles.effects;

import com.karuslabs.commons.animation.particles.Particles;
import com.karuslabs.commons.animation.particles.effect.Context;
import com.karuslabs.commons.world.BoundLocation;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.util.Vector;


public class StubContext<Origin extends BoundLocation, Target extends BoundLocation> implements Context<Origin, Target> {
    
    Origin origin;
    Target target;
    long current;
    long iterations;
    Location location;
    ThreadLocalRandom random;
    Vector vector;
    
    
    public StubContext(Origin origin, Target target, long current, long iterations) {
        this.origin = origin;
        this.target = target;
        this.current = current;
        this.iterations = iterations;
        this.random = ThreadLocalRandom.current();
    }
    
    
    @Override
    public void render(Particles particles, Location location) {
        this.location = location.clone();
    }
    
    @Override
    public Origin getOrigin() {
        return origin;
    }

    @Override
    public Target getTarget() {
        return target;
    }
    
    @Override
    public Vector getVector() {
        if (vector == null) {
            vector = new Vector();
        }
        return vector;
    }

    @Override
    public long getCurrent() {
        return current;
    }

    @Override
    public long getIterations() {
        return iterations;
    }

    @Override
    public void cancel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
