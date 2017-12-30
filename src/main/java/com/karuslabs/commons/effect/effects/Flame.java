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


/**
 * Represents a flame.
 * <br>
 * <img src = "https://thumbs.gfycat.com/SentimentalSphericalHydatidtapeworm-size_restricted.gif" alt = "Flame.gif">
 */
@Immutable
public class Flame implements Effect {
    
    private Particles flame;
    private int total;
    
    
    /**
     * Constructs a {@code Flame} with the specified particles and the default total number of particles, 10.
     * 
     * @param flame the particles 
     */
    public Flame(Particles flame) {
        this(flame, 10);
    }
    
    /**
     * Constructs a {@code Flame} with the specified particles and total number of particles.
     * 
     * @param flame the particles
     * @param total the total number of particles
     */
    public Flame(Particles flame, int total) {
        this.flame = flame;
        this.total = total;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void render(Context context, Location origin, Location target, Vector offset) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        
        for (int i = 0; i < total; i += flame.getAmount()) {
            randomCircle(offset).multiply(random.nextDouble(0, 0.6));
            offset.setY(random.nextFloat() * 1.8);

            context.render(flame, origin, offset);
        }
    }
    
}
