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

import org.bukkit.util.Vector;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


class WaveTest extends EffectBase {
    
    WaveData front = mock(WaveData.class);
    WaveData back = mock(WaveData.class);
    Wave wave = spy(new Wave(PARTICLES, COLOURED, front, back, 20, 5).get());
    
    
    @ParameterizedTest
    @CsvSource({"1, 0", "0, 1"})
    void render(int current, int times) {
        context.current = current;
        doNothing().when(wave).process(location);
        
        Vector water = new Vector(0, 0, 1);
        Vector cloud = new Vector(0, 0, 2);
        
        wave.waters.add(water);
        wave.clouds.add(cloud);
        
        
        wave.render(context);
        
        verify(wave, times(times)).process(location);
        verify(context).render(PARTICLES, location, water);
        verify(context).render(COLOURED, location, cloud);
    }
    
    
    @Test
    void process() {
        wave.waters.add(vector);
        wave.clouds.add(vector);
        
        wave.process(location);
        
        verify(front).process(eq(wave.waters), eq(wave.clouds), any(), eq(20), eq(5.0F), eq(0.5235988f));
        verify(back).process(eq(wave.waters), eq(wave.clouds), any(), eq(20), eq(5.0F), eq(0.5235988f));
        assertTrue(wave.waters.isEmpty());
        assertTrue(wave.clouds.isEmpty());
    }
    
}
