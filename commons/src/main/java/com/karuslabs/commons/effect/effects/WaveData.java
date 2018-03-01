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
package com.karuslabs.commons.effect.effects;

import java.util.Set;
import java.util.function.Predicate;

import org.bukkit.util.Vector;

import static com.karuslabs.commons.world.Vectors.rotateAroundYAxis;
import static java.lang.Math.pow;


/**
 * Represents the data for rendering the {@code Wave}.
 */
public class WaveData {
    
    int count;
    Vector copy;
    Vector middle;
    float length;
    float height;
    Vector relative;
    Vector vector;
    
    
    /**
     * Constructs a {@code WaveData} with the specified total number of particles, height, rise and end.
     * 
     * @param count the total number of particles
     * @param height the height
     * @param rise the offset to which the wave is increased
     * @param end the offset to which the wave is displaced at the end
     */
    public WaveData(int count, float height, Vector rise, Vector end) {
        this.count = count;
        this.height = height;
        
        middle = rise.clone().subtract(end);
        copy = end.clone().add(middle.clone().multiply(0.5));
        length = (float) middle.length();
        relative = middle.clone().multiply(1f / length);
        vector = new Vector(middle.getY(), -middle.getX(), 0).normalize();
        
        if (vector.getX() < 0) {
            vector.multiply(-1);
        }
    }
    
    
    void process(Set<Vector> waters, Set<Vector> clouds, Predicate<Integer> range, int rows, float width, float yaw) {
        for (int i = 0; i < count; i++) {
            float ratio = (float) i / count;
            float x = (ratio - .5f) * length;
            float y = (float) (-height / pow((length / 2), 2) * pow(x, 2) + height);

            Vector v = copy.clone();
            
            v.add(relative.clone().multiply(x));
            v.add(vector.clone().multiply(y));
            
            if (range.test(i)) { 
                processRows(clouds, v, rows, width, yaw);
            } else {
                processRows(waters, v, rows, width, yaw);
            }
        }
    }
    
    void processRows(Set<Vector> cache, Vector v, int rows, float width, float yaw) {
        for (int j = 0; j < rows; j++) {
            float z = ((float) j / rows - .5f) * width;
            Vector vec = v.clone().setZ(v.getZ() + z);
            rotateAroundYAxis(vec, yaw);

            cache.add(vec);
        }
    }
    
    
}
