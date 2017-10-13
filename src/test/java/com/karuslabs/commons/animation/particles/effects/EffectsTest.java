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
import org.bukkit.util.Vector;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static com.karuslabs.commons.animation.particles.effects.Constants.NONE;
import static java.lang.Math.PI;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


public class EffectsTest extends Effect {
    
    @ParameterizedTest
    @MethodSource("render_parameters")
    public void render(Task<Task, BoundLocation, BoundLocation> task, Vector expected) {
        task.render(context);
        
        verify(context).render(any(Particles.class), any(Location.class));
        
        assertVector(expected, context.location);
    }
    
    static Stream<Arguments> render_parameters() {
        return Stream.of(
            of(new AnimatedBall(PARTICLES).get(), from(1, 2, 1.8)),
            of(new Arc(PARTICLES, 2, 1).get(), from(1, 1.0000000121882824, 1)),
            of(new Circle(PARTICLES).get(), from(1.4000000059604645, 1, 1)),            
            of(new Composite(PARTICLES).get(), from(1, 1, 1)),
            of(new Cone(PARTICLES, 0.5F, 0.006F, PI / 16, 180, 1, 0, false).get(), from(1, 1, 1)),
            of(new Donut(PARTICLES, 1, 1, 2, 0.5F, NONE).get(), from(3.5, 1, 1)),
            of(new Helix(new StandardParticles(null, 1, 0, 0, 0, 0), 1, 1, 10, 10, PI / 4).get(), from(8.071067811865564, 1, 8.071067811865387)),
            of(new Vortex(PARTICLES, 2, 0.5f, PI / 16, 1, 1).get(), from(2, 1, 2.732050807568877)),
            of(new Warp(PARTICLES, 1, 1, 0.2f, 12).get(), from(2, 1, 1))
        );
    }
    
}
