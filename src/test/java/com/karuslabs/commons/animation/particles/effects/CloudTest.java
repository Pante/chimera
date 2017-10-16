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
package com.karuslabs.commons.animation.particles.effects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.mockito.Mockito.*;


class CloudTest extends Base {

    Cloud cloud = spy(new Cloud(PARTICLES, COLOURED).get());
    
    
    @Test
    void render() {
        doNothing().when(cloud).renderCloud(context, location, RANDOM, 50);
        doNothing().when(cloud).renderDroplets(context, location, RANDOM, 15);
        
        cloud.render(context);
        
        verify(cloud).renderCloud(context, location, RANDOM, 50);
        verify(cloud).renderDroplets(context, location, RANDOM, 15);
    }
    
    
    @Test
    void renderCloud() {
        cloud.renderCloud(context, location, random, 1);
        
        verify(context).render(PARTICLES, location);
    }
    
    
    @ParameterizedTest
    @CsvSource({"0, 2", "1, 0"})
    void renderDroplets(int number, int expected) {
        when(random.nextInt(2)).thenReturn(number);
        
        cloud.renderDroplets(context, location, random, 1);
        
        verify(context, times(expected)).render(COLOURED, location);
    }
    
}
