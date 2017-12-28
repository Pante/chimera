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

import com.karuslabs.commons.effect.particles.Particles;

import org.bukkit.Location;
import org.bukkit.util.Vector;


/**
 * Represents the context in which an {@code Effect} is executed.
 */
public interface Context {
    
    /**
     * Renders the {@code Particles} at the specified {@code Location}.
     * 
     * @param particles the particles
     * @param location the location
     */
    public void render(Particles particles, Location location);
    
    /**
     * Renders the {@code Particles} at the specified {@code Location} with the specified
     * offset.
     * 
     * @param particles the particles
     * @param location the location
     * @param offset the offset
     */
    public void render(Particles particles, Location location, Vector offset);
    
    
    /**
     *  Cancels the execution of the {@code Effect}.
     */
    public void cancel();
    
    
    /**
     * Returns the number of steps.
     * 
     * @return the steps
     */
    public int steps();
    
    /**
     * Sets the number of steps.
     * 
     * @param steps the steps
     */
    public void steps(int steps);
    
}
