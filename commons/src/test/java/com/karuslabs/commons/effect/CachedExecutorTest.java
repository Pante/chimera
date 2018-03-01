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

import java.util.function.Consumer;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.ArgumentCaptor;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class CachedExecutorTest {
    
    static Player player = mock(Player.class);
    
    Plugin plugin = mock(Plugin.class);
    Effect effect = mock(Effect.class);
    CachedExecutor executor = spy(CachedExecutor.builder(plugin).async(true).effect(effect).build());
    
    ArgumentCaptor<Task> captor = forClass(Task.class);
    Particles particles = mock(Particles.class);
    Location location = mock(Location.class);
    
    
    @ParameterizedTest
    @MethodSource("render_parameters")
    void render(Consumer<CachedExecutor> consumer, Class<? extends Task> expected) {
        doReturn(null).when(executor).schedule(any());
        
        consumer.accept(executor);
        
        verify(executor).schedule(captor.capture());
        verify(effect).get();
        
        assertEquals(expected, captor.getValue().getClass());
    }
    
    static Stream<Arguments> render_parameters() {
        return Stream.of(
            of(infer(executor -> executor.render(null, null)), Tasks.GlobalTask.class),
            of(infer(executor -> executor.render(singleton(player), null, null)), Tasks.PlayersTask.class),
            of(infer(executor -> executor.render(player, null, null)), Tasks.PlayerTask.class)
        );
    }
    
    static Object infer(Consumer<CachedExecutor> consumer) {
        return consumer;
    }
    
}
