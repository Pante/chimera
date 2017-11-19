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
package com.karuslabs.commons.animation.particles;

import com.karuslabs.commons.annotation.Immutable;

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
    
    
    /**
     * Returns the maximum random offset on the X axis.
     * 
     * @return the offset on the X axis
     */
    public double getOffsetX() {
        return offsetX;
    }
    
    /**
     * Returns the maximum random offset on the Y axis.
     * 
     * @return the offset on the Y axis
     */
    public double getOffsetY() {
        return offsetY;
    }
    
    /**
     * Returns the maximum random offset on the Z axis.
     * 
     * @return the offset on the Z axis
     */
    public double getOffsetZ() {
        return offsetZ;
    }

    /**
     * Returns the speed.
     * 
     * @return the speed
     */
    public double getSpeed() {
        return speed;
    }

    
    static abstract class AbstractBuilder<GenericBuilder extends AbstractBuilder, GenericParticles extends AbstractParticles> extends Builder<GenericBuilder, GenericParticles> {

        AbstractBuilder(GenericParticles particles) {
            super(particles);
        }
        
        /**
         * Sets the maximum random offset on the X axis.
         * 
         * @param x the maximum offset on the X axis
         * @return this
         */
        public GenericBuilder offsetX(double x) {
            particles.offsetX = x;
            return getThis();
        }
        
        /**
         * Sets the maximum random offset on the Y axis.
         * 
         * @param y the maximum offset on the Y axis
         * @return this
         */
        public GenericBuilder offsetY(double y) {
            particles.offsetY = y;
            return getThis();
        }
        
        /**
         * Sets the maximum random offset on the Z axis.
         * 
         * @param z the maximum offset on the Z axis
         * @return this
         */
        public GenericBuilder offsetZ(double z) {
            particles.offsetZ = z;
            return getThis();
        }
        
        /**
         * Sets the speed.
         * 
         * @param speed the speed
         * @return this
         */
        public GenericBuilder speed(double speed) {
            particles.speed = speed;
            return getThis();
        }
        
    }
    
}
