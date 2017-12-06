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

import org.bukkit.plugin.Plugin;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.mockito.Mockito.*;


class EffectExecutorTest {
    
    Plugin plugin = mock(Plugin.class);
    EffectExecutor executor = spy(new StubBuilder(plugin).orientate(true).iterations(1).delay(2).period(3).async(true).build());
  
    
    @ParameterizedTest
    @CsvSource({"true, 1, 0", "false, 0, 1"})
    void schedule(boolean async, int asyncTimes, int syncTimes) {
        executor.async = async;
        Task task = mock(Task.class);
        
        executor.schedule(task);
        
        verify(task, times(asyncTimes)).runTaskTimerAsynchronously(plugin, 2, 3);
        verify(task, times(syncTimes)).runTaskTimer(plugin, 2, 3);
    }
    
    
    static class StubBuilder extends EffectExecutor.Builder<EffectExecutor, StubBuilder> {

        public StubBuilder(Plugin plugin) {
            super(new EffectExecutor(plugin, false, 0, 0, 0, false) {});
        }

        @Override
        protected StubBuilder getThis() {
            return this;
        }
        
    }
    
}
