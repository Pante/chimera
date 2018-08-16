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

import java.util.concurrent.Callable;
import java.util.function.Consumer;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;


abstract class BukkitScheduledExecutor implements ScheduledExecutor {
    
    BukkitScheduler scheduler;
    Plugin plugin;
    
    
    BukkitScheduledExecutor(Plugin plugin) {
        this.scheduler = plugin.getServer().getScheduler();
        this.plugin = plugin;
    }
    
    
    @Override
    public <T> Result<T> submit(Callable<T> callable, long delay) {
        return submit(new ResultTask(callable), delay);
    }

    @Override
    public <T> Result<T> submit(Runnable runnable, T result, long delay) {
        return submit(new ResultTask<>(runnable, result), delay);
    }

    @Override
    public Result<?> submit(Runnable runnable, long delay) {
        return submit(new ResultTask<>(runnable, null), delay);
    }
    
    protected abstract <T> Result<T> submit(ResultTask<T> task, long delay);
    

    @Override
    public <T> Result<T> schedule(Consumer<Callback> consumer, long period, long delay) {
        return schedule(new BukkitResultTask<>(consumer), period, delay);
    }

    @Override
    public <T> Result<T> schedule(Runnable runnable, T result, long period, long delay) {
        return schedule(new BukkitResultTask<>(runnable, result), period, delay);
    }

    @Override
    public Result<?> schedule(Runnable runnable, long period, long delay) {
        return schedule(new BukkitResultTask<>(runnable, null), period, delay);
    }
    
    protected abstract <T> Result<T> schedule(BukkitResultTask<T> task, long period, long delay);
    
}


class AsynchronousScheduledExecutor extends BukkitScheduledExecutor {

    AsynchronousScheduledExecutor(Plugin plugin) {
        super(plugin);
    }

    
    @Override
    protected <T> Result<T> submit(ResultTask<T> task, long delay) {
        scheduler.runTaskLaterAsynchronously(plugin, task, delay);
        return task;
    }

    @Override
    protected <T> Result<T> schedule(BukkitResultTask<T> task, long period, long delay) {
        task.set(scheduler.runTaskTimerAsynchronously(plugin, task, delay, period));
        return task;
    }
    
}


class SynchronousScheduledExecutor extends BukkitScheduledExecutor {

    SynchronousScheduledExecutor(Plugin plugin) {
        super(plugin);
    }

    
    @Override
    protected <T> Result<T> submit(ResultTask<T> task, long delay) {
        scheduler.runTaskLater(plugin, task, delay);
        return task;
    }

    @Override
    protected <T> Result<T> schedule(BukkitResultTask<T> task, long period, long delay) {
        task.set(scheduler.runTaskTimer(plugin, task, delay, period));
        return task;
    }
    
}