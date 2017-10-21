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

import com.karuslabs.commons.util.concurrent.ScheduledPromiseTask;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.Test;

import static com.karuslabs.commons.animation.screen.StubBar.builder;
import static com.karuslabs.commons.locale.MessageTranslation.NONE;
import static java.util.Collections.EMPTY_LIST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class BarTest {
    
    Plugin plugin = mock(Plugin.class);
    ScheduledPromiseTask<?> task = mock(ScheduledPromiseTask.class);
    Bar bar = spy(builder(plugin, task).translation(NONE).iterations(1).infinite().delay(2).period(3).build());
    
    
    @Test
    void render_Player() {
        assertEquals(task, bar.render(mock(Player.class)));
    }
    
    
    @Test
    void render_Players() {
        assertEquals(task, bar.render(EMPTY_LIST));
        verify(task).runTaskTimerAsynchronously(plugin, 2, 3);
    }
    
}
