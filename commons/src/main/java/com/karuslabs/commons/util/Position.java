/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
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
package com.karuslabs.commons.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;


public class Position extends Vector {
    
    public static final int X = 1;
    public static final int Y = 2;
    public static final int Z = 3;
    
    
    private boolean rotate = false;
    private boolean[] relative = { false, false, false };
    
    
    public Position() {
        super();
    }

    public Position(int x, int y, int z) {
        super(x, y, z);
    }

    public Position(double x, double y, double z) {
        super(x, y, z);
    }

    public Position(float x, float y, float z) {
        super(x, y, z);
    }
    
    
    public void apply(Location anchor) {
        if (rotate) {
            Vectors.rotate(this, anchor);
            
        } else {
            if (relative(X)) setX(x + anchor.getX());
            if (relative(Y)) setY(Y + anchor.getY());
            if (relative(Z)) setZ(z + anchor.getZ());
        }
    }
    
    
    public boolean rotate() {
        return rotate;
    }
    
    public Position rotate(boolean rotate) {
        this.rotate = rotate;
        return this;
    }
    
    
    public boolean relative(int axis) {
        return relative[axis];
    }
    
    public Position relative(int axis, boolean relative) {
        this.relative[axis] = relative;
        return this;
    }
    
}
