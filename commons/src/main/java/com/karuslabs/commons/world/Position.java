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
package com.karuslabs.commons.world;

import org.bukkit.util.Vector;

import static java.lang.Math.abs;


/**
 * Represents a mutable vector which additionally consists of the yaw and pitch.
 */
public class Position extends Vector {
    
    private static final double DELTA = 0.000001;
    
    private float yaw;
    private float pitch;
    
    
    /**
     * Constructs an empty {@code Position}.
     */
    public Position() {
        this(0, 0, 0, 0, 0);
    }
    
    /**
     * Constructs a {@code Position} with the specified X, Y and Z coordinates, yaw and pitch.
     * 
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @param yaw the yaw
     * @param pitch the pitch 
     */
    public Position(double x, double y, double z, float yaw, float pitch) {
        super(x, y, z);
        this.yaw = yaw;
        this.pitch = pitch;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Position add(Vector vector) {
        super.add(vector);
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Position subtract(Vector vector) {
        super.subtract(vector);
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Position multiply(Vector vector) {
        super.multiply(vector);
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Vector divide(Vector vector) {
        super.divide(vector);
        return this;
    }

    
    /**
     * Returns the yaw.
     * 
     * @return the yaw
     */
    public float getYaw() {
        return yaw;
    }
    
    /**
     * Sets the yaw.
     * 
     * @param yaw the yaw
     */
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
    
    /**
     * Returns the pitch.
     * 
     * @return the pitch
     */
    public float getPitch() {
        return pitch;
    }

    /**
     * Sets the pitch.
     * 
     * @param pitch the pitch
     */
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
    
    
    @Override
    public Position clone() {
        return (Position) super.clone();
    }
    
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
            
        } else if (other instanceof Position) {
            return equals((Position) other);
            
        } else {
            return false;
        }
    }
    
    public boolean equals(Position other) {
        return abs(other.x - x) < DELTA && abs(other.y - y) < DELTA && abs(other.z - z) < DELTA 
            && abs(other.yaw - yaw) < DELTA && abs(other.pitch - pitch) < DELTA;
    }
    
    
    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 53 * hash + Float.hashCode(yaw);
        hash = 53 * hash + Float.hashCode(pitch);
        return hash;
    }
    
    @Override
    public String toString() {
        return "Position[" + x + ", " + y + ", " + z + ", " + yaw + ", " + pitch + "]"; 
    }
    
}
