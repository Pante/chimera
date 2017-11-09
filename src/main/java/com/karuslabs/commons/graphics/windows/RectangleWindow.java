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
package com.karuslabs.commons.graphics.windows;

import com.karuslabs.commons.annotation.Immutable;
import com.karuslabs.commons.graphics.Point;
import com.karuslabs.commons.graphics.regions.Region;
import com.karuslabs.commons.locale.MessageTranslation;

import java.util.List;


public class RectangleWindow extends Window {
    
    private int width;
    private int height;
    
    
    public RectangleWindow(List<Region> regions, MessageTranslation translation, boolean reset, int width, int height) {
        super(regions, translation, reset);
        this.width = width;
        this.height = height;
    }

    
    @Override
    public @Immutable Point at(int slot) {
        int x = (slot % width) - 1;
        int y = slot / width;
        return new Point(x, y);
    }
    
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
}
