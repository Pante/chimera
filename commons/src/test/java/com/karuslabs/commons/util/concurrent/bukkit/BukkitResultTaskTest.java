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

package com.karuslabs.commons.util.concurrent.bukkit;

import org.bukkit.scheduler.BukkitTask;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BukkitResultTaskTest {
    
    BukkitTask task = mock(BukkitTask.class);
    BukkitResultTask result = new BukkitResultTask<>(() -> {}, "result");
    
    
    @Test
    void cancel() {
        result.setTask(task);
        
        assertTrue(result.cancel(false));
        verify(task).cancel();
    }
    
    
    @Test
    void cancel_throws_exception() {
        doThrow(IllegalArgumentException.class).when(task).cancel();
        result.setTask(task);
        
        assertFalse(result.cancel(false));
    }
    
    
    @Test
    void cancel_null() {
        assertFalse(result.cancel(false));
    }
    
    
    @Test
    void setTask_throws_exception() {
        var result = new BukkitResultTask<>(() -> "");
        result.setTask(task);
        
        assertEquals("BukkitTask has been set", assertThrows(IllegalStateException.class, () -> result.setTask(task)).getMessage());
    }
    
}
