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
package com.karuslabs.commons.effect;


/**
 * An implementation of {@code Effect} which may be reset.
 */
public abstract class ResettableEffect implements Effect {
    
    /**
     * The maximum number of steps before this effect should be reset.
     */
    protected int steps;
    
    
    /**
     * Constructs a {@code ResettablEffect} with the specified maximum number of steps.
     * 
     * @param steps the maximum number of steps before this effect should be reset.
     */
    public ResettableEffect(int steps) {
        this.steps = steps;
    }
    
    
    /**
     * Returns {@code true}, or {@code false} if the specified number of steps is less than the maximum number of steps.
     * 
     * @param steps the number of steps
     * @return true if the specified number of steps is greater than or equal to the maximum number of steps; else false
     */
    @Override
    public boolean reset(int steps) {
        return this.steps <= steps;
    }
    
}
