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
package com.karuslabs.commons.animation;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static java.util.Collections.singleton;
import static org.bukkit.Sound.WEATHER_RAIN;
import static org.bukkit.SoundCategory.MASTER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class AudioTest extends Base {
    
    static Audio equality = new Audio(WEATHER_RAIN, 0.5F, 0.75F);
    Audio audio = spy(new Audio(WEATHER_RAIN, 0.5F, 0.75F));

    
    @Test
    void play_Location() {
        audio.play(location);
        
        verify(world).playSound(location, WEATHER_RAIN, MASTER, 0.5F, 0.75F);
    }
    
    
    @Test
    void play_Player() {
        audio.play(player);
        
        verify(audio).play(player, location);
    }
    
    
    @Test
    void play_Players_Location() {
        audio.play(singleton(player), location);
        
        verify(audio).play(player, location);
    }
    
    
    @Test
    void play_Player_Location() {
        audio.play(player, location);
        
        verify(player).playSound(location, WEATHER_RAIN, MASTER, 0.5F, 0.75F);
    }
    
    
    @ParameterizedTest
    @MethodSource("equality_parameters")
    void equals_Object(Object other, boolean expected) {
        assertEquals(expected, equality.equals(other));
    }
    
    
    @ParameterizedTest
    @MethodSource("equality_parameters")
    void hashcode(Object other, boolean expected) {
        assertEquals(expected, equality.hashCode() == other.hashCode());
    }
    
    static Stream<Arguments> equality_parameters() {
        return Stream.of(
            of(equality, true),
            of(new Audio(WEATHER_RAIN, 0.5F, 0.75F), true),
            of(new Audio(WEATHER_RAIN, 0.6F, 0.75F), false),
            of(new Object(), false)
        );
    }
    
}
