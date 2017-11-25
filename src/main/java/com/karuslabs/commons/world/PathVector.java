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


public class PathVector extends Vector {
    
    private static final double DELTA = 0.000001;
    
    private float yaw;
    private float pitch;
    
    
    public PathVector() {
        this(0, 0, 0, 0, 0);
    }
    
    public PathVector(double x, double y, double z, float yaw, float pitch) {
        super(x, y, z);
        this.yaw = yaw;
        this.pitch = pitch;
    }
    
    
    @Override
    public PathVector add(Vector vector) {
        super.add(vector);
        return this;
    }
    
    @Override
    public PathVector subtract(Vector vector) {
        super.subtract(vector);
        return this;
    }
    
    @Override
    public PathVector multiply(Vector vector) {
        super.multiply(vector);
        return this;
    }

    @Override
    public Vector divide(Vector vector) {
        super.divide(vector);
        return this;
    }

            
    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
    
    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
    
    
    @Override
    public PathVector clone() {
        return (PathVector) super.clone();
    }
    
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
            
        } else if (other instanceof PathVector) {
            return equals((PathVector) other);
            
        } else {
            return false;
        }
    }
    
    public boolean equals(PathVector other) {
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
        return "PathVector[" + x + ", " + y + ", " + z + ", " + yaw + ", " + pitch + "]"; 
    }
    
}
