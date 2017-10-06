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


public class PathVector extends Vector {
    
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
    
    
    public float yaw() {
        return yaw;
    }

    public void yaw(float yaw) {
        this.yaw = yaw;
    }

    public float pitch() {
        return pitch;
    }

    public void pitch(float pitch) {
        this.pitch = pitch;
    }
    
    
    @Override
    public PathVector clone() {
        return (PathVector) super.clone();
    }
    
}
