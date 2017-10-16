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

import com.karuslabs.commons.animation.particles.effects.DiscoBall.Direction;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static com.karuslabs.commons.animation.particles.effects.DiscoBall.Direction.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class DiscoBallTest extends Base {
    
    DiscoBall ball = spy(new DiscoBall(PARTICLES, COLOURED).get());
    
    
    @Test
    void render() {
        doNothing().when(ball).renderSphere(context, location);
        doNothing().when(ball).renderLines(context, location, RANDOM);
        
        ball.render(context);
        
        verify(ball).renderSphere(context, location);
        verify(ball).renderLines(context, location, RANDOM);
    }
    
    
    @Test
    void renderLine() {
        ThreadLocalRandom random = spy(RANDOM);
        doReturn(2).when(random).nextInt(anyInt(), anyInt());
        
        ball.renderLines(context, location, random);
        
        verify(context, times(4)).render(COLOURED, location);
        assertVector(from(1.06, 1.06, 1.06), context.location);
    }
    
    
    @ParameterizedTest
    @MethodSource("calculateY_arguments")
    void calculateY(Direction direction, int down, int up, int both) {
        when(random.nextInt(anyInt(), anyInt())).thenReturn(1);
        ball.direction = direction;
        
        ball.calculateY(random);
        
        verify(random, times(down)).nextInt(15, 30);
        verify(random, times(up)).nextInt(-30, -15);
        verify(random, times(both)).nextInt(-15, 15);
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
        ball.renderSphere(context, location);
        
        verify(context).render(PARTICLES, location);
    }
    
}
