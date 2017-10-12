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
import com.karuslabs.commons.util.concurrent.ScheduledPromiseTask;
import com.karuslabs.commons.world.BoundLocation;

import java.util.function.BiConsumer;

import org.bukkit.Location;
import org.bukkit.util.Vector;


class EffectTask<Origin extends BoundLocation, Target extends BoundLocation> extends ScheduledPromiseTask<Void> implements Context<Origin, Target> {

    protected Task<Task, Origin, Target> task;
    protected BiConsumer<Particles, Location> render;
    protected Origin origin;
    protected Target target;
    protected boolean orientate;
    protected Vector vector;
    
    
    EffectTask(Task<Task, Origin, Target> task, BiConsumer<Particles, Location> render, Origin origin, Target target, boolean orientate, long iterations) {
        super(iterations);
        this.task = task;
        this.render = render;
        this.origin = origin;
        this.target = target;
        this.orientate = orientate;
    }

    @Override
    protected void process() {
        if (origin.validate() && target.validate()) {
            origin.update();
            target.update();
            if (orientate) {
                orientate();
            }
            task.render(this);
            
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
    
    
    public Task<Task, Origin, Target> getTask() {
        return task;
    }
    
    @Override
    public void render(Particles particles, Location location) {
        render.accept(particles, location);
    }
    
    @Override
    public Vector getVector() {
        if (vector == null) {
            vector = new Vector();
        }
        
        return vector;
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
    protected Void value() {
        return null;
    }

}
