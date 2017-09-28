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


public class Direction {
    
    private float yaw;
    private float pitch;
    private boolean relative;
    
    
    public Direction() {
        this(0, 0, false);
    }
    
    public Direction(float yaw, float pitch, boolean relative) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.relative = relative;
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
    
    
    public boolean isRelative() {
        return relative;
    }
    
    public void setRelative(boolean relative) {
        this.relative = relative;
    }
    
}
