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


/**
 * Represents a computation which renders {@code Effect}s.
 */
public abstract class Task extends ScheduledResultTask<Void> implements Context {
    
    /**
     * The effect to render.
     */
    protected Effect effect;
    BoundLocation origin;
    BoundLocation target;
    boolean orientate;
    Vector vector;
    int steps;
    
    
    /**
     * Constructs a task with the specified {@code Effect}, origin, target, orientation and iterations.
     * 
     * @param effect the effect
     * @param origin the origin
     * @param target the target
     * @param orientate true if the direction of the origin and target will be updated; else false
     * @param iterations the number of iterations
     */
    public Task(Effect effect, BoundLocation origin, BoundLocation target, boolean orientate, long iterations) {
        super(iterations);
        this.effect = effect;
        this.origin = origin;
        this.target = target;
        this.orientate = orientate;
        this.vector = new Vector();
        this.steps = 0;
    }
    
    
    /**
     * Validates, updates and orientates the origin and target before rendering the {@code Effect}.
     */
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
    
    /**
     * Sets the direction of the origin and target.
     */
    protected void orientate() {
        Location origin = this.origin.getLocation();
        Location target = this.target.getLocation();
        
        Vector direction = target.toVector().subtract(origin.toVector());
        
        origin.setDirection(direction);
        target.setDirection(direction.multiply(-1));
    }
    
    /**
     * Renders the {@code Effect}, resetting and incrementing the {@code Effect} if necessary.
     */
    protected void render() {
        if (effect.reset(steps)) {
            steps = 0;
        }

        effect.render(this, origin.getLocation(), target.getLocation(), vector);
        
        if (effect.isIncremental()) {
            steps++;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int steps() {
        return steps;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void steps(int steps) {
        this.steps = steps;
    }

    /**
     * Returns {@code null}.
     * 
     * @return null
     */
    @Override
    protected Void value() {
        return null;
    }
    
}
