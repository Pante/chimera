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
package com.karuslabs.commons.animation.particles.effect;

import com.karuslabs.commons.animation.particles.Particles;
import com.karuslabs.commons.world.BoundLocation;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;


public class EffectTest {
    
    private Plugin plugin;
    private Task<Task, BoundLocation, BoundLocation> task;
    private Effect<BoundLocation, BoundLocation> effect;
    private Player player;
    private ArgumentCaptor<EffectTask<BoundLocation, BoundLocation>> captor;
    private Particles particles;
    private Location location;
    
    
    public EffectTest() {
        plugin = mock(Plugin.class);
        task = mock(Task.class);
        effect = spy(Effect.builder(plugin).task(task).orientate(true).iterations(1).delay(2).period(3).async(true).build());
        player = mock(Player.class);
        captor = forClass(EffectTask.class);
        particles = mock(Particles.class);
        location = mock(Location.class);
    }
    
    
    @Test
    public void render() {
        doReturn(null).when(effect).schedule(any());
        
        effect.render(null, null);
        
        verify(effect).schedule(captor.capture());
        verify(task).get();
        
        captor.getValue().render.accept(particles, location);
        verify(particles).render(location);
    }
    
    
    @Test
    public void render_Player() {
        doReturn(null).when(effect).schedule(any());
        
        effect.render(player, null, null);
        
        verify(effect).schedule(captor.capture());
        verify(task).get();
        
        captor.getValue().render.accept(particles, location);
        verify(particles).render(player, location);
    }
    
    
    @Test
    public void render_Players() {
        doReturn(null).when(effect).schedule(any());
        ArgumentCaptor<Set<Player>> players = forClass(Set.class);
        
        effect.render(singleton(player), null, null);
        
        verify(effect).schedule(captor.capture());
        verify(task).get();
        
        captor.getValue().render.accept(particles, location);
        verify(particles).render(players.capture(), eq(location));
        assertEquals(singleton(player), players.getValue());
    }
    
    
    @ParameterizedTest
    @CsvSource({"true, 1, 0", "false, 0, 1"})
    public void schedule_Parameters(boolean async, int asyncTimes, int syncTimes) {
        effect.async = async;
        EffectTask<BoundLocation, BoundLocation> task = mock(EffectTask.class);
        
        effect.schedule(task);
        
        verify(task, times(asyncTimes)).runTaskTimerAsynchronously(plugin, 2, 3);
        verify(task, times(syncTimes)).runTaskTimer(plugin, 2, 3);
    }
    
}
