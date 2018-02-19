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

import com.karuslabs.commons.effect.particles.Particles;

import java.util.Set;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static java.util.Collections.singleton;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class TasksTest {
    
    static final Player player = mock(Player.class);
    static final Set<Player> set = singleton(player);
    static final Location location = mock(Location.class);
    
    Particles particles = mock(Particles.class);
    Vector offset = new Vector();
    
    Task global = Tasks.global(null, null, null, true, 0);
    Task players = Tasks.players(null, set, null, null, true, 0);
    
    
    @Test
    void global_render() {
        global.render(particles, location);
        
        verify(particles).render(location);
    }
    
    
    @Test
    void global_render_Offset() {
        global.render(particles, location, offset);
        
        verify(particles).render(location, offset);
    }
    
    
    @ParameterizedTest
    @MethodSource("single_parameters")
    void single_render(Player player, int times) {
        Task single = Tasks.player(null, player, null, null, true, 0);
        
        single.render(particles, location);
        
        verify(particles, times(times)).render(player, location);
    }
    
    @ParameterizedTest
    @MethodSource("single_parameters")
    void single_render_Offset(Player player, int times) {
        Task single = Tasks.player(null, player, null, null, true, 0);
        
        single.render(particles, location, offset);
        
        verify(particles, times(times)).render(player, location, offset);
    }
    
    static Stream<Arguments> single_parameters() {
        return Stream.of(of(player, 1), of(null, 0));
    }
    
    
    @Test
    void players_render() {
        players.render(particles, location);
        verify(particles).render(set, location);
    }
    
    
    @Test
    void players_render_Offset() {
        players.render(particles, location, offset);
        verify(particles).render(set, location, offset);
    }
    
}
