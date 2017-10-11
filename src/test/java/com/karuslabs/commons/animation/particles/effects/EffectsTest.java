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

import com.karuslabs.commons.animation.particles.*;
import com.karuslabs.commons.animation.particles.effect.*;
import com.karuslabs.commons.world.*;

import java.util.stream.Stream;

import org.bukkit.Location;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static java.lang.Math.PI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


public class EffectsTest {
    
    private static final double ROUNDING_ERROR = 0.000000000000001;
    private static final StandardParticles STANDARD = new StandardParticles(null, 50, 0, 0, 0, 0);
    
    private StaticLocation origin = new StaticLocation(new Location(null, 1, 1, 1), null, false);
    private StaticLocation target = new StaticLocation(new Location(null, 2, 2, 2), null, false);
    
    
    @ParameterizedTest
    @MethodSource("render_parameters")
    public void render(Task<Task, BoundLocation, BoundLocation> task, double x, double y, double z) {
        StubContext<BoundLocation, BoundLocation> context = spy(new StubContext<>(origin, target, 0, 0));
        
        task.render(context);
        
        verify(context).render(any(Particles.class), any(Location.class));
        
        assertEquals(x, context.location.getX(), ROUNDING_ERROR);
        assertEquals(y, context.location.getY(), ROUNDING_ERROR);
        assertEquals(z, context.location.getZ(), ROUNDING_ERROR);
    }
    
    static Stream<Arguments> render_parameters() {
        return Stream.of(
            of(new AnimatedBall(STANDARD).get(), 1.0, 2.0, 1.8),
            of(new Arc(STANDARD, 2, 1).get(), 1.0, 1.0000000121882824, 1.0),
            of(new Circle(STANDARD).get(), 1.4000000059604645, 1.0, 1.0),
            of(new Cone(STANDARD, 0.5F, 0.006F, PI / 16, 180, 1, 0, false).get(), 1.0, 1.0, 1.0),
            of(new Helix(new StandardParticles(null, 1, 0, 0, 0, 0), 1, 1, 10, 10, PI / 4).get(), 8.071067811865564, 1.0, 8.071067811865387),
            of(new Vortex(STANDARD, 2, 0.5f, PI / 16, 1, 1).get(), 3.0, 1.0, 1.0),
            of(new Warp(STANDARD, 1, 1, 0.2f, 12).get(), 2.0, 1.0, 1.0)
        );
    }
    
}
