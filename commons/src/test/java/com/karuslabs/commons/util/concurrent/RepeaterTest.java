/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
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
package com.karuslabs.commons.util.concurrent;

import java.util.concurrent.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RepeaterTest {
    
    Repeater repeater = spy(new Repeater(1));
    RunnableScheduledFuture<?> task = mock(RunnableScheduledFuture.class);
    
    
    @Test
    void schedule_runnable() {
        doReturn(null).when(repeater).scheduleAtFixedRate(any(), anyInt(), anyInt(), any());
        
        repeater.schedule(() -> {}, 1, 2, TimeUnit.DAYS, 4);
        
        verify(repeater).scheduleAtFixedRate(any(RunnableRepetition.class), eq(1L), eq(2L), eq(TimeUnit.DAYS));
    }
    
    
    @Test
    void schedule_repetition() {
        doReturn(null).when(repeater).scheduleAtFixedRate(any(), anyInt(), anyInt(), any());
        
        var repetition = new RunnableRepetition(() -> {}, 3);
        repeater.schedule(repetition, 1, 2, TimeUnit.DAYS);
        
        verify(repeater).scheduleAtFixedRate(repetition, 1, 2, TimeUnit.DAYS);
    }
    
    
    @Test
    void decorateTask_runnable() {
        Runnable runnable = mock(Runnable.class);
        
        assertEquals(task, repeater.decorateTask(runnable, task));
        
        verifyNoInteractions(runnable);
        verifyNoInteractions(task);
    }
    
    
    @Test
    void decorateTask_continual() {
        Repetition continual = mock(Repetition.class);
        
        assertEquals(task, repeater.decorateTask(continual, task));
        
        verify(continual).set(task);
    }
    
}
