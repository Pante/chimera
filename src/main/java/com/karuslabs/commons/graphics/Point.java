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

import java.util.function.Supplier;


@ValueBased
public class Point implements Comparable<Point>, Supplier<Point> {
    
    public int x;
    public int y;
    
    
    public Point() {
        this(0, 0);
    }
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    
    @Override
    public int compareTo(Point other) {
        if (y < other.y || (y == other.y && x < other.x)) {
            return -1;

        } else if (y == other.y && x == other.x) {
            return 0;

        } else {
            return 1;
        }
    }
    
    public Point move(int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }
    
    public Point set(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }
    
    
    @Override
    public Point get() {
        return new Point(x, y);
    }
    
    public boolean equals(Point point) {
        return x == point.x && y == point.y;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object instanceof Point) {
            Point point = (Point) object;
            return x == point.x && y == point.y;
            
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return x * 151 + y;
    }
    
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
    
}
