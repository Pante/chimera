/*
 * Copyright (C) 2017 Karus Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.karuslabs.commons.core.test;

import com.karuslabs.commons.concurrency.UncheckedExecutionException;

import com.karuslabs.commons.core.annotations.Supported;

import java.util.List;
import java.util.concurrent.*;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.*;

/**
 * Represents a stub BukkitScheduler implementation.
 * <br><b>Warning:</b> Only methods denoted by {@link com.karuslabs.commons.core.annotations.Supported} are implemented.
 */
public class StubScheduler implements BukkitScheduler {

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, Runnable task, long delay) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, BukkitRunnable task, long delay) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, Runnable task) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, BukkitRunnable task) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int scheduleSyncRepeatingTask(Plugin plugin, Runnable task, long delay, long period) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int scheduleSyncRepeatingTask(Plugin plugin, BukkitRunnable task, long delay, long period) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task, long delay) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int scheduleAsyncRepeatingTask(Plugin plugin, Runnable task, long delay, long period) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * Runs the task on the calling thread.
     * 
     * @param <T> the task's return type
     * @param plugin a plugin
     * @param task the task to run
     * @return FutureTask related to the task
     */
    @Override
    @Supported
    public <T> Future<T> callSyncMethod(Plugin plugin, Callable<T> task) {
        try {
            task.call();
        } catch (Exception e) {
            throw new UncheckedExecutionException();
        }
        
        return new FutureTask<>(task);
    }
    
    /**
     * Does nothing.
     * 
     * @param taskId the task id
     */
    @Override
    @Supported
    public void cancelTask(int taskId) {}
    
    /**
     * Does nothing.
     * 
     * @param plugin the plugin
     */
    @Override
    @Supported
    public void cancelTasks(Plugin plugin) {}

    @Override
    public void cancelAllTasks() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isCurrentlyRunning(int taskId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isQueued(int taskId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<BukkitWorker> getActiveWorkers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<BukkitTask> getPendingTasks() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * Runs the task on the calling thread.
     * 
     * @param plugin the plugin
     * @param task the task to run
     * @return <code>null</code>
     */
    @Override
    @Supported
    public BukkitTask runTask(Plugin plugin, Runnable task) throws IllegalArgumentException {
        task.run();
        return null;
    }
    
    /**
     * Runs the task on the calling thread.
     * 
     * @param plugin the plugin
     * @param task the task to run
     * @return <code>null</code>
     */
    @Override
    @Supported
    public BukkitTask runTask(Plugin plugin, BukkitRunnable task) throws IllegalArgumentException {
        task.run();
        return null;
    }
    
    /**
     * Runs the task on the calling thread.
     * 
     * @param plugin the plugin
     * @param task the task to run
     * @return <code>null</code>
     */
    @Override
    @Supported
    public BukkitTask runTaskAsynchronously(Plugin plugin, Runnable task) throws IllegalArgumentException {
        task.run();
        return null;
    }

    /**
     * Runs the task on the calling thread.
     * 
     * @param plugin the plugin
     * @param task the task to run
     * @return <code>null</code>
     */
    @Override
    @Supported
    public BukkitTask runTaskAsynchronously(Plugin plugin, BukkitRunnable task) throws IllegalArgumentException {
        task.run();
        return null;
    }

    @Override
    public BukkitTask runTaskLater(Plugin plugin, Runnable task, long delay) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BukkitTask runTaskLater(Plugin plugin, BukkitRunnable task, long delay) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BukkitTask runTaskLaterAsynchronously(Plugin plugin, Runnable task, long delay) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BukkitTask runTaskLaterAsynchronously(Plugin plugin, BukkitRunnable task, long delay) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BukkitTask runTaskTimer(Plugin plugin, Runnable task, long delay, long period) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BukkitTask runTaskTimer(Plugin plugin, BukkitRunnable task, long delay, long period) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BukkitTask runTaskTimerAsynchronously(Plugin plugin, Runnable task, long delay, long period) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BukkitTask runTaskTimerAsynchronously(Plugin plugin, BukkitRunnable task, long delay, long period) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
