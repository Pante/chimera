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

import com.karuslabs.commons.animation.screen.ProgressBar.ScheduledTask;
import com.karuslabs.commons.annotation.JDK9;

import java.util.function.BiConsumer;

import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.Test;

import static java.util.Collections.singleton;
import static org.mockito.Mockito.*;


public class ProgressBarTest {
    
    private Player player = mock(Player.class);
    private BossBar boss = mock(BossBar.class);
    private BiConsumer<BossBar, Context> consumer = mock(BiConsumer.class);
    private ProgressBar bar = ProgressBar.builder(null).supplier(() -> boss).consumer(() -> consumer).build();
    
    
    @Test
    @JDK9
    public void process() {
        ScheduledTask task = (ScheduledTask) bar.newTask(singleton(player));
        
        task.process();
        
        verify(boss).addPlayer(player);
        verify(consumer).accept(boss, task);
        
    }
    
    
    @Test
    @JDK9
    public void callback() {
        ScheduledTask task = (ScheduledTask) bar.newTask(singleton(player));
        
        task.callback();
        
        verify(boss).removeAll();
    }
    
}
