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
package com.karuslabs.commons.effect.effects;

import com.karuslabs.commons.effect.particles.Particles;
import com.karuslabs.commons.effect.*;

import java.util.*;

import org.bukkit.*;
import org.bukkit.util.Vector;

import static java.lang.Math.*;


/**
 * Represents a wave.
 * <br>
 * <img src = "https://thumbs.gfycat.com/MildTheseFinwhale-size_restricted.gif" alt = "Wave.gif">
 */
public class Wave extends IncrementalEffect {
    
    private static final Vector RISE = new Vector(0.75, 0.5, 0);
    private static final WaveData FRONT = new WaveData(10, 1, RISE, new Vector(-1.5, 0, 0));
    private static final WaveData BACK =  new WaveData(10, 2, RISE, new Vector(3, 0, 0));
    
    
    private Particles water;
    private Particles cloud;
    private WaveData front;
    private WaveData back;
    private int rows;
    private float width;
    private int max;
    Set<Vector> waters;
    Set<Vector> clouds;
    
    
    /**
     * Constructs a {@code Wave} with the specified water and cloud particles, the default total number of steps, 50, 
     * data for rendering the front and back, number of rows, 20 and width, 5.
     * 
     * @param water the particles of which the water is composed
     * @param cloud the particles of which the cloud is composed
     */
    public Wave(Particles water, Particles cloud) {
        this(water, cloud, 50, FRONT, BACK, 20, 5);
    }
    
    /**
     * Constructs a {@code Wave} with the specified water and cloud particles, total number of steps, data for rendering the front and back,
     * number of rows and width.
     * 
     * @param water the particles of which the water is composed
     * @param cloud the particles of which the cloud is composed
     * @param steps the total number of steps
     * @param front the data for rendering the front
     * @param back the data for rendering the back
     * @param rows the number of rows
     * @param width the width
     */
    public Wave(Particles water, Particles cloud, int steps, WaveData front, WaveData back, int rows, float width) {
        super(steps);
        this.water = water;
        this.cloud = cloud;
        this.front = front;
        this.back = back;
        this.rows = rows;
        this.width = width;
        max = front.count - 1;
        waters = new HashSet<>();
        clouds = new HashSet<>();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void render(Context context, Location origin, Location target, Vector velocity) {
        if (context.steps() == 0) {
            velocity.copy(origin.getDirection().setY(0).normalize().multiply(0.2));
            process(origin);
        }

        origin.add(velocity);

        for (Vector v : waters) {
            context.render(water, origin, v);
        }
                
        for (Vector v : clouds) {
            context.render(cloud, origin, v);
        }
    }
    
    void process(Location location) {
        waters.clear();
        clouds.clear();
        
        float yaw = (float) ((-location.getYaw() + 90) * (PI / 180));
        front.process(waters, clouds, i -> i == 0 || i == max, rows, width, yaw);
        back.process(waters, clouds, i -> i == max, rows, width, yaw);
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public Wave get() {
        return new Wave(water, cloud, steps, front, back, rows, width);
    }

}
