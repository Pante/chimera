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

import com.karuslabs.commons.annotation.Shared;

import java.util.function.Supplier;

import org.bukkit.Location;
import org.bukkit.util.Vector;


/**
 * Represents an {@code Effect} composed of {@code Particles}.
 */
public interface Effect extends Supplier<Effect> {
    
    public void render(Context context, @Shared Location origin, @Shared Location target, @Shared Vector offset);
    
    
    /**
     * Returns {@code true}, or {@code false} if this {@code Effect} should not reset.
     * The default implementation will always return {@code false}.
     * 
     * Implementations may override this method to specify the criteria for resetting this {@code Effect}.
     * 
     * @param steps the current number of steps
     * @return true if the effect should reset; else false
     */
    public default boolean reset(int steps) {
        return false;
    }
    
    /**
     * Returns {@code true}, or {@code false} if this {@code Effect} is not incremental.
     * The default implementation will always return {@code false}.
     * 
     * Implementations may override this method to signify that it is incremental.
     * 
     * @return true if the effect is incremental; else false
     */
    public default boolean isIncremental() {
        return false;
    }
    
    /**
     * 
     * Returns a copy of this {@code Effect}.
     * The default implementation will always return {@code this}.
     * 
     * Implementations which are mutable should override this method to customise the copying.
     * 
     * @return the Effect 
     */
    @Override
    public default Effect get() {
        return this;
    }
    
}
