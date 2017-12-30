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
import static java.lang.Math.*;


/**
 * Represents a tornado.
 * <br>
 * <img src = "https://thumbs.gfycat.com/NextMagnificentArrowworm-size_restricted.gif" alt = "Tornado.gif">
 */
@Immutable
public class Tornado implements Effect {
    
    private Particles tornado;
    private Particles cloud;
    private float height;
    private float radius;
    private float cloudSize;
    private double yOffset;
    private double distance;
    
    
    /**
     * Constructs a {@code Tornado} with the specified tornado and cloud particles, 
     * the default height, 5, radius, 5, cloud size, 2.5, Y offset, 0.8 and distance between each row, 0.375.
     * 
     * @param tornado the particles of which the tornado is composed
     * @param cloud the particles of which the cloud is composed
     */
    public Tornado(Particles tornado, Particles cloud) {
        this(tornado, cloud, 5F, 5F, 2.5F, 0.8, 0.375);
    }
    
    /**
     * Constructs a {@code Tornado} with the specified tornado and cloud particles, height, radius, cloud size, Y offset and distance
     * between each row.
     * 
     * @param tornado the particles of which the tornado is composed
     * @param cloud the particles of which the cloud is composed
     * @param height the height
     * @param radius the radius
     * @param cloudSize the cloud size
     * @param yOffset the Y offset
     * @param distance the distance between each row
     */
    public Tornado(Particles tornado, Particles cloud, float height, float radius, float cloudSize, double yOffset, double distance) {
        this.tornado = tornado;
        this.cloud = cloud;
        this.height = height;
        this.radius = radius;
        this.cloudSize = cloudSize;
        this.yOffset = yOffset;
        this.distance = distance;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void render(Context context, Location origin, Location target, Vector offset) {
        renderCloud(context, origin, offset, ThreadLocalRandom.current());
        renderTornado(context, origin, offset);
    }
    
    void renderCloud(Context context, Location location, Vector offset, ThreadLocalRandom random) {
        for (int i = 0; i < 100 * cloudSize; i += cloud.getAmount()) {
            randomCircle(offset).multiply(random.nextDouble() * cloudSize);
            context.render(cloud, location, offset.setY(offset.getY() + yOffset));
        }
    }
    
    void renderTornado(Context context, Location location, Vector offset) {
        double r = 0.45 * (radius * (2.35 / height));
        for (double y = 0; y < height; y += distance) {
            double fr = r * y;
            if (fr > radius) {
                fr = radius;
            }
            renderTornadoPortion(context, location, offset, fr, y);
        }
    }
    
    void renderTornadoPortion(Context context, Location location, Vector offset, double radius, double y) {
        offset.setY(y + 0.2);
        double amount = radius * 64;
        double inc = 2 * PI / amount;
        
        for (int i = 0; i < amount; i += tornado.getAmount()) {
            double angle = i * inc;
            offset.setX(radius * cos(angle)).setZ(radius * sin(angle));
            context.render(tornado, location, offset);
        }
    }
    
}
