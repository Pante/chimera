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

import com.karuslabs.commons.annotation.Immutable;
import com.karuslabs.commons.effect.*;
import com.karuslabs.commons.effect.particles.Particles;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import static com.karuslabs.commons.world.Vectors.randomCircle;


@Immutable
public class Cloud implements Effect {
 
    private Particles cloud;
    private Particles droplets;
    private int cloudTotal;
    private int dropletsTotal;
    private float size;
    private float radius;
    private float offsetY;
    
    
    public Cloud(Particles cloud, Particles droplets) {
        this(cloud, droplets, 50, 15, 0.7F, 0.7F - 0.1F, 0.8F);
    }
    
    public Cloud(Particles cloud, Particles droplets, int cloudTotal, int dropletsTotal, float size, float radius, float offsetY) {
        this.cloud = cloud;
        this.droplets = droplets;
        this.cloudTotal = cloudTotal;
        this.dropletsTotal = dropletsTotal;
        this.size = size;
        this.radius = radius;
        this.offsetY = offsetY;
    }

    
    @Override
    public void render(Context context, Location origin, Location target, Vector offset) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        renderCloud(context, origin, offset, random);
        renderDroplets(context, origin, offset, random);
    }
    
    void renderCloud(Context context, Location location, Vector offset, ThreadLocalRandom random) {
        for (int i = 0; i < cloudTotal; i+= cloud.getAmount()) {
            randomCircle(offset).multiply(random.nextFloat() * size);
            context.render(cloud, location, offset.setY(offsetY));
        }
    }
    
    void renderDroplets(Context context, Location location, Vector offset, ThreadLocalRandom random) {
        offset.setY(offsetY + 0.2);
        for (int i = 0; i < dropletsTotal; i+= droplets.getAmount()) {
            if (random.nextBoolean()) {
                float x = random.nextFloat() * radius;
                float z = random.nextFloat() * radius;
                
                context.render(droplets, location, offset.setX(x).setZ(z));
                context.render(droplets, location, offset.setX(-x).setZ(-z));
            }
        }
    }
    
}