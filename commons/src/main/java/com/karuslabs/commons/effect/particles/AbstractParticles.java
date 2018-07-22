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
package com.karuslabs.commons.effect.particles;

import com.karuslabs.annotations.Immutable;

import org.bukkit.*;


@Immutable
abstract class AbstractParticles extends Particles {
    
    protected double offsetX;
    protected double offsetY;
    protected double offsetZ;
    protected double speed;
    
    
    AbstractParticles(Particle particle, int amount, double offsetX, double offsetY, double offsetZ, double speed) {
        super(particle, amount);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.speed = speed;
    }
    
    
    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public double getOffsetZ() {
        return offsetZ;
    }

    public double getSpeed() {
        return speed;
    }

    
    static abstract class AbstractBuilder<GenericBuilder extends AbstractBuilder, GenericParticles extends AbstractParticles> extends Builder<GenericBuilder, GenericParticles> {

        AbstractBuilder(GenericParticles particles) {
            super(particles);
        }
        
        
        public GenericBuilder offsetX(double x) {
            particles.offsetX = x;
            return self();
        }
        
        public GenericBuilder offsetY(double y) {
            particles.offsetY = y;
            return self();
        }
        
        public GenericBuilder offsetZ(double z) {
            particles.offsetZ = z;
            return self();
        }
        
        public GenericBuilder speed(double speed) {
            particles.speed = speed;
            return self();
        }
        
    }
    
}
