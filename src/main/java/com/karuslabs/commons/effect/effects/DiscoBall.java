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

import static com.karuslabs.commons.effect.effects.DiscoBall.Direction.DOWN;
import static com.karuslabs.commons.world.Vectors.random;


/**
 * Represents a disco ball.
 * <img src = "https://thumbs.gfycat.com/MintyGlisteningDog-size_restricted.gif" alt = "Discoball.gif">
 */
@Immutable
public class DiscoBall implements Effect {
    
    /**
     * Represents the direction of the lines.
     */
    public enum Direction {
        UP, DOWN, BOTH;
    }
    
    private Particles sphere;
    private Particles line;
    private int sphereTotal;
    private int lineTotal;
    private float radius;
    private int lines;
    private int lineLength;
    Direction direction;
    
    /**
     * Constructs a {@code DiscoBall} with the specified sphere and line particles, the default total number of sphere particles (50), 
     * total number of line particles (100), radius (0.6), number of lines (7), length of each line (15) and direction of the lines (DOWN).
     * 
     * @param sphere the particles of which the sphere is composed
     * @param line the particles of which the line is composed
     */
    public DiscoBall(Particles sphere, Particles line) {
        this(sphere, line, 50, 100, 0.6F, 7, 15, DOWN);
    }
    
    /**
     * Constructs a {@code DiscoBall} with the specified sphere and line particles, total number of sphere particles, total number of line particles,
     * radius, number of lines, length of each line and direction of the lines.
     * 
     * @param sphere the particles of which the sphere is composed
     * @param line the particles of which the line is composed
     * @param sphereTotal the total number of sphere particles
     * @param lineTotal the total number of line particles
     * @param radius the radius
     * @param lines the total number of lines
     * @param lineLength the length of each line
     * @param direction  the direction of the lines
     */
    public DiscoBall(Particles sphere, Particles line, int sphereTotal, int lineTotal, float radius, int lines, int lineLength, Direction direction) {
        this.sphere = sphere;
        this.line = line;
        this.sphereTotal = sphereTotal;
        this.lineTotal = lineTotal;
        this.radius = radius;
        this.lines = lines;
        this.lineLength = lineLength;
        this.direction = direction;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void render(Context context, Location origin, Location target, Vector offset) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        
        renderLines(context, origin, offset, random);
        renderSphere(context, origin, offset);
    }
    
    void renderLines(Context context, Location location, Vector offset, ThreadLocalRandom random) {
        int max = random.nextInt(2, lines) * 2;
        for (int i = 0; i < max; i++) {
            offset.setX(random.nextInt(-lineLength, lineLength)).setY(-calculateY(random)).setZ(random.nextInt(-lineLength, lineLength));
            double ratio = offset.length() / lineTotal;
            offset.normalize().multiply(ratio);
            
            double x = offset.getX();
            double y = offset.getY();
            double z = offset.getZ();
            
            offset.setX(0).setY(0).setZ(0);
            for (int j = 0; j < lineTotal; j += line.getAmount()) {
                context.render(line, location, offset);
                offset.setX(offset.getX() + x).setY(offset.getY() + y).setZ(offset.getZ() + z);
            }
        }
    }
    
    int calculateY(ThreadLocalRandom random) {
        switch (direction) {
            case DOWN:
                return random.nextInt(lineLength, lineLength * 2);
                
            case UP:
                return random.nextInt(-2 * lineLength, -lineLength);
                
            default:
                return random.nextInt(-lineLength, lineLength);
        }
    }
    
    void renderSphere(Context context, Location location, Vector offset) {
        for (int i = 0; i < sphereTotal; i += sphere.getAmount()) {
            random(offset).multiply(radius);
            context.render(sphere, location, offset);
        }
    }
    
}
