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

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BukkitExecutorTest {
    
    BukkitExecutor executor;
    BukkitScheduler scheduler;
    Plugin plugin;
    
    
    BukkitExecutorTest() {
        scheduler = mock(BukkitScheduler.class);
        Server server = when(mock(Server.class).getScheduler()).thenReturn(scheduler).getMock();
        plugin = when(mock(Plugin.class).getServer()).thenReturn(server).getMock();
        executor = new AsyncBukkitExecutor(plugin);
    }
    
    
    @Test
    void submit_callable() {
        executor.submit(() -> "");
        verify(scheduler).runTaskLaterAsynchronously(eq(plugin), any(Runnable.class), eq(0L));
    }
    
    
    @Test
    void submit_runnable() {
        executor.submit(() -> {}, "");
        verify(scheduler).runTaskLaterAsynchronously(eq(plugin), any(Runnable.class), eq(0L));
    }
    
    
    @Test
    void submit_runnable_null() {
        executor.submit(() -> {});
        verify(scheduler).runTaskLaterAsynchronously(eq(plugin), any(Runnable.class), eq(0L));
    }
    
    
    @Test
    void schedule_consumer() {
        executor.schedule(callback -> {}, 1);
        verify(scheduler).runTaskTimerAsynchronously(eq(plugin), any(Runnable.class), eq(0L), eq(1L));
    }
    
    
    @Test
    void schedule_runnable() {
        executor.schedule(() -> {}, "", 1);
        verify(scheduler).runTaskTimerAsynchronously(eq(plugin), any(Runnable.class), eq(0L), eq(1L));
    }
    
    
    @Test
    void schedule_runnable_null() {
        executor.schedule(() -> {}, 1);
        verify(scheduler).runTaskTimerAsynchronously(eq(plugin), any(Runnable.class), eq(0L), eq(1L));
    }
    
}


@ExtendWith(MockitoExtension.class)
class AsyncBukkitExecutorTest {
    
    AsyncBukkitExecutor executor;
    BukkitScheduler scheduler;
    Plugin plugin;
    BukkitResultTask<String> result;
    ScheduledBukkitResultTask<String> scheduled;
    BukkitTask task;
    
    
    AsyncBukkitExecutorTest() {
        result = new BukkitResultTask<>(() -> {}, null);
        scheduled = new ScheduledBukkitResultTask<>(() -> {}, null);
        task = mock(BukkitTask.class);
        scheduler = when(mock(BukkitScheduler.class).runTaskLaterAsynchronously(any(Plugin.class), any(Runnable.class), anyLong())).thenReturn(task).getMock();
        when(scheduler.runTaskTimerAsynchronously(any(Plugin.class), any(Runnable.class), anyLong(), anyLong())).thenReturn(task);
        Server server = when(mock(Server.class).getScheduler()).thenReturn(scheduler).getMock();
        plugin = when(mock(Plugin.class).getServer()).thenReturn(server).getMock();
        executor = new AsyncBukkitExecutor(plugin);
    }
    
    
    @Test
    void submit() {
        executor.submit(result, 0);
        
        verify(scheduler).runTaskLaterAsynchronously(eq(plugin), any(Runnable.class), eq(0L));
        assertThrows(IllegalStateException.class, () -> result.setTask(task));
    }
    
    
    @Test
    void schedule() {
        executor.schedule(scheduled, 1, 0);
        
        verify(scheduler).runTaskTimerAsynchronously(eq(plugin), any(Runnable.class), eq(0L), eq(1L));
        assertThrows(IllegalStateException.class, () -> scheduled.setTask(task));
    }
    
}


@ExtendWith(MockitoExtension.class)
class SyncBukkitExecutorTest {
    
    SyncBukkitExecutor executor;
    BukkitScheduler scheduler;
    Plugin plugin;
    BukkitResultTask<String> result;
    ScheduledBukkitResultTask<String> scheduled;
    BukkitTask task;
    
    
    SyncBukkitExecutorTest() {
        result = new BukkitResultTask<>(() -> {}, null);
        scheduled = new ScheduledBukkitResultTask<>(() -> {}, null);
        task = mock(BukkitTask.class);
        scheduler = when(mock(BukkitScheduler.class).runTaskLater(any(Plugin.class), any(Runnable.class), anyLong())).thenReturn(task).getMock();
        when(scheduler.runTaskTimer(any(Plugin.class), any(Runnable.class), anyLong(), anyLong())).thenReturn(task);
        Server server = when(mock(Server.class).getScheduler()).thenReturn(scheduler).getMock();
        plugin = when(mock(Plugin.class).getServer()).thenReturn(server).getMock();
        executor = new SyncBukkitExecutor(plugin);
    }
    
    
    @Test
    void submit() {
        executor.submit(result, 0);
        
        verify(scheduler).runTaskLater(eq(plugin), any(Runnable.class), eq(0L));
        assertThrows(IllegalStateException.class, () -> result.setTask(task));
    }
    
    
    @Test
    void schedule() {
        executor.schedule(scheduled, 1, 0);
        
        verify(scheduler).runTaskTimer(eq(plugin), any(Runnable.class), eq(0L), eq(1L));
        assertThrows(IllegalStateException.class, () -> scheduled.setTask(task));
    }
    
}