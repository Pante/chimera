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
package com.karuslabs.commons.effect;

import com.karuslabs.commons.util.concurrent.ScheduledResultTask;
import com.karuslabs.commons.world.BoundLocation;

import org.bukkit.Location;
import org.bukkit.util.Vector;


public abstract class Task extends ScheduledResultTask<Void> implements Context {
    
    protected Effect effect;
    BoundLocation origin;
    BoundLocation target;
    boolean orientate;
    Vector vector;
    int steps;
    
    
    public Task(Effect effect, BoundLocation origin, BoundLocation target, boolean orientate, long iterations) {
        super(iterations);
        this.effect = effect;
        this.origin = origin;
        this.target = target;
        this.orientate = orientate;
        this.vector = new Vector();
        this.steps = 0;
    }
    
    
    @Override
    protected void process() {
        if (origin.validate() && target.validate()) {
            origin.update();
            target.update();
            if (orientate) {
                orientate();
            }
            render();
            
        } else {
            done();
        }
    }
    
    protected void orientate() {
        Location origin = this.origin.getLocation();
        Location target = this.target.getLocation();
        
        Vector direction = target.toVector().subtract(origin.toVector());
        
        origin.setDirection(direction);
        target.setDirection(direction.multiply(-1));
    }
    
    protected void render() {
        if (effect.reset(steps)) {
            steps = 0;
        }
        
        effect.render(this, origin.getLocation(), target.getLocation(), vector);
        
        if (effect.isIncremental()) {
            steps++;
        }
    }

    @Override
    public int steps() {
        return steps;
    }

    @Override
    public void steps(int steps) {
        this.steps = steps;
    }

    @Override
    protected Void value() {
        return null;
    }
    
}
