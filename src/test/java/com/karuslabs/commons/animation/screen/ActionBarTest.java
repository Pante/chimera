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
package com.karuslabs.commons.animation.screen;

import com.karuslabs.commons.animation.screen.ActionBar.ScheduledTask;

import java.util.function.BiFunction;

import org.bukkit.entity.Player;

import org.junit.jupiter.api.Test;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class ActionBarTest {
    
    private ActionBar bar;
    private BiFunction<Player, Context, String> function;
    private Player player;
    private StubSpigot spigot;
    
    
    public ActionBarTest() {
        function = when(mock(BiFunction.class).apply(any(), any())).thenReturn("value").getMock();
        bar = ActionBar.builder(null).function(function).build();
        player = when(mock(Player.class).spigot()).thenReturn(spigot = new StubSpigot()).getMock();
    }
    
    
    @Test
    public void process() {
        ScheduledTask task = (ScheduledTask) bar.task(singletonList(player));
        
        task.process();

        assertEquals("value", spigot.values[0].toPlainText());
        verify(function).apply(player, task);
    }
    
}
