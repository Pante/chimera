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

import com.karuslabs.commons.util.concurrent.*;

import org.bukkit.plugin.Plugin;


/**
 * Represents a executor for {@code Effect}s.
 */
public abstract class EffectExecutor {
    
    protected Plugin plugin;
    protected boolean orientate;
    protected long iterations;
    protected long delay;
    protected long period;
    protected boolean async;
    
    
    /**
     * Constructs an {@code EffectExecutor} with the specified plugin, orientation, number of iterations, delay, period and whether the effect
     * should be executed asynchronously.
     * 
     * @param plugin the plugin
     * @param orientate true if the direction of the origin and target will be updated; else false
     * @param iterations the number of iterations
     * @param delay the number of ticks to wait before executing the effect
     * @param period the number of ticks to wait between runs
     * @param async true if the effect is to be executed asynchronously; else false
     */
    public EffectExecutor(Plugin plugin, boolean orientate, long iterations, long delay, long period, boolean async) {
        this.plugin = plugin;
        this.orientate = orientate;
        this.iterations = iterations;
        this.delay = delay;
        this.period = period;
        this.async = async;
    }
    
    /**
     * Schedules the specified {@code Task} to be executed.
     * 
     * @param task the task
     * @return the Result for the specified Task
     */
    protected Result<Void> schedule(Task task) {
        if (async) {
            task.runTaskTimerAsynchronously(plugin, delay, period);
            
        } else {
            task.runTaskTimer(plugin, delay, period);
        }
        
        return task;
    }
    
    
    /**
     * Represents a builder for {@code GenericExecutor}s.
     * 
     * @param <GenericExecutor> the subclass of EffectExecutor
     * @param <GenericBuilder> the subclass of Builder
     */
    public static abstract class Builder<GenericExecutor extends EffectExecutor, GenericBuilder extends Builder> {
        
        /**
         * The {@code GenericExecutor} to build.
         */
        protected GenericExecutor executor;

        
        /**
         * Constructs the {@code Builder} with the specified {@code GenericExecutor}.
         * 
         * @param executor the executor
         */
        public Builder(GenericExecutor executor) {
            this.executor = executor;
        }
        
        
        /**
         * Returns a generic version of {@code this}.
         *
         * @return this
         */
        protected abstract GenericBuilder getThis();
        
        
        /**
         * Sets whether the direction of the origin and target should be updated.
         * 
         * @param orientate true if the direction of the origin and target should be updated; else false
         * @return this
         */
        public GenericBuilder orientate(boolean orientate) {
            executor.orientate = orientate;
            return getThis();
        }
        
        /**
         * Sets whether the {@code Effect} is to be executed asynchronously.
         * 
         * @param async true if the effect is to be executed asynchronously; else false
         * @return this
         */
        public GenericBuilder async(boolean async) {
            executor.async = async;
            return getThis();
        }
        
        /**
         * Sets the effect to be executed infinitely.
         * 
         * @return this
         */
        public GenericBuilder infinite() {
            executor.iterations = ScheduledResultTask.INFINITE;
            return getThis();
        }
        
        /**
         * Sets the number of iterations.
         * 
         * @param iterations the number of iterations
         * @return this
         */
        public GenericBuilder iterations(long iterations) {
            executor.iterations = iterations;
            return getThis();
        }
        
        /**
         * Sets the ticks to wait before executing the {@code Effect}.
         * 
         * @param delay the ticks to wait
         * @return this
         */
        public GenericBuilder delay(long delay) {
            executor.delay = delay;
            return getThis();
        }
        
        /**
         * Sets the ticks to wait between runs.
         * 
         * @param period the ticks to wait
         * @return this
         */
        public GenericBuilder period(long period) {
            executor.period = period;
            return getThis();
        }
        
        /**
         * Builds the {@code GenericExecutor}.
         * 
         * @return the executor
         */
        public GenericExecutor build() {
            return executor;
        }

    }
    
}
