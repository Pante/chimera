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
package com.karuslabs.commons.graphics.regions;

import com.karuslabs.commons.graphics.buttons.Button;
import com.karuslabs.commons.graphics.*;

import java.util.*;


public class Rectangle<GenericButton extends Button> extends AbstractRegion<GenericButton> {
    
    private Point corner;
    private int width;
    private int height;

    
    public Rectangle(Point corner, int width, int height) {
        this(new HashMap<>(), corner, width, height);
    }
    
    public Rectangle(Map<Point, GenericButton> map, Point corner, int width, int height) {
        super(map);
        this.corner = corner;
        this.width = width;
        this.height = height;
    }
    
    
    @Override
    public boolean contains(Point point) {
        return (corner.x <= point.x && point.x <= corner.x + width) && (corner.y <= point.y && point.y <= corner.y + height);
    }
    
    @Override
    public int size() {
        return width * height;
    }
    
    public Point getCorner() {
        return corner;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
}
