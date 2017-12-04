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

import com.karuslabs.commons.effect.particles.MaterialParticles;
import com.karuslabs.commons.effect.particles.Particles;
import com.karuslabs.commons.effect.particles.ColouredParticles;
import com.karuslabs.commons.effect.particles.StandardParticles;
import com.karuslabs.commons.animation.particles.*;
import com.karuslabs.commons.world.*;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import static org.bukkit.Color.WHITE;
import static org.mockito.Mockito.*;


public abstract class EffectBase extends VectorBase {
    
    public static final Particles PARTICLES = new StandardParticles(null, 100, 0, 0, 0, 1);
    public static final Particles COLOURED = new ColouredParticles(null, WHITE, 100);
    public static final Particles MATERIAL = new MaterialParticles(null, null, 100, 0, 0, 0, 0);
    public static final Particles SINGLE = new StandardParticles(null, 1, 0, 0, 0, 1);
    public static final Particles SPAM = new StandardParticles(null, 1000, 0, 0, 0, 1);
    
    public static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    
    public Location location = new Location(null, 1, 1, 1, 60, 120);
    public StaticLocation original = new StaticLocation(location, null, false);
    
    public Location target = new Location(null, 2, 2, 2, 30, 90);
    public StaticLocation targeted = new StaticLocation(target, null, false);
    
    public StubContext context = spy(new StubContext(original, targeted, 0, 0));
    
    
    public Vector vector = context.vector;
    public ThreadLocalRandom random = mock(ThreadLocalRandom.class);
    
}
