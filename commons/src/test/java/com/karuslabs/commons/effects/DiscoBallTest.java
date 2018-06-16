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
package com.karuslabs.commons.effects;

import com.karuslabs.commons.effects.DiscoBall.Direction;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static com.karuslabs.commons.effects.DiscoBall.Direction.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class DiscoBallTest extends EffectBase {
    
    DiscoBall ball = spy(new DiscoBall(PARTICLES, COLOURED));
    
    
    @Test
    void render() {
        doNothing().when(ball).renderSphere(context, origin, offset);
        doNothing().when(ball).renderLines(context, origin, offset, RANDOM);
        
        ball.render(context, origin, target, offset);
        
        verify(ball).renderSphere(context, origin, offset);
        verify(ball).renderLines(context, origin, offset, RANDOM);
    }
    
    
    @Test
    void renderLine() {
        doReturn(2).when(mockRandom).nextInt(2, 7);
        doReturn(-2).when(mockRandom).nextInt(-15, 15);
        doReturn(-2).when(ball).calculateY(any());
        
        ball.renderLines(context, origin, offset, mockRandom);
        
        assertVector(from(-0.020000000000000004,0.020000000000000004,-0.020000000000000004), offset);
    }
    
    
    @ParameterizedTest
    @MethodSource("calculateY_arguments")
    void calculateY(Direction direction, int down, int up, int both) {
        when(mockRandom.nextInt(anyInt(), anyInt())).thenReturn(1);
        ball.direction = direction;
        
        ball.calculateY(mockRandom);
        
        verify(mockRandom, times(down)).nextInt(15, 30);
        verify(mockRandom, times(up)).nextInt(-30, -15);
        verify(mockRandom, times(both)).nextInt(-15, 15);
    }
    
    static Stream<Arguments> calculateY_arguments() {
        return Stream.of(
            of(DOWN, 1, 0, 0),
            of(UP, 0, 1, 0),
            of(BOTH, 0, 0, 1)
        );
    }
    
    
    @Test
    void renderSphere() {
        ball.renderSphere(context, origin, offset);
        
        verify(context).render(PARTICLES, origin, offset);
    }
    
}
