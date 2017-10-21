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
import com.karuslabs.commons.animation.particles.effect.*;

import java.util.*;

import org.bukkit.*;
import org.bukkit.util.Vector;

import static java.lang.Math.*;


public class Wave implements Task<Wave> {
    
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
    
    
    public Wave(Particles water, Particles cloud) {
        this(water, cloud, FRONT, BACK, 20, 5);
    }
    
    public Wave(Particles water, Particles cloud, WaveData front, WaveData back, int rows, float width) {
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


    @Override
    public void render(Context context) {
        Location location = context.getOrigin().getLocation();
        Vector velocity = context.getVector();

        if (context.getCurrent() == 0) {
            velocity.copy(location.getDirection().setY(0).normalize().multiply(0.2));
            process(location);
        }

        location.add(velocity);

        for (Vector v : waters) {
            context.render(water, location, v);
        }
                
        for (Vector v : clouds) {
            context.render(cloud, location, v);
        }
    }
    
    void process(Location location) {
        waters.clear();
        clouds.clear();
        
        float yaw = (float) ((-location.getYaw() + 90) * (PI / 180));
        front.process(waters, clouds, i -> i == 0 || i == max, rows, width, yaw);
        back.process(waters, clouds, i -> i == max, rows, width, yaw);
    }

    
    @Override
    public Wave get() {
        return new Wave(water, cloud, front, back, rows, width);
    }

}
