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
package com.karuslabs.commons.graphics;

import com.karuslabs.commons.annotation.ValueBased;


@ValueBased
public final class Point implements Comparable<Point> {
    
    public final int x;
    public final int y;
    private final int area;
    private int hash;
    
    
    public Point() {
        this(0, 0);
    }
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        area = x * y;
        hash = 0;
    }
    
    
    @Override
    public int compareTo(Point other) {
        if (area > other.area) {
            return 1;

        } else if (area == other.area) {
            return 0;

        } else {
            return -1;
        }
    }

    
    public boolean equals(Point point) {
        return x == point.x && y == point.y;
    }
    
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
            
        } else if (object instanceof Point) {
            Point point = (Point) object;
            return x == point.x && y == point.y;
            
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        if (hash == 0) {
            hash = hash();
        }
        return hash;
    }
    
    private int hash() {
        return 31 * (217 + x) + y;
    }
    
    
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
    
}
