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


public abstract class EffectExecutor {
    
    protected Plugin plugin;
    protected boolean orientate;
    protected long iterations;
    protected long delay;
    protected long period;
    protected boolean async;
    
    
    public EffectExecutor(Plugin plugin, boolean orientate, long iterations, long delay, long period, boolean async) {
        this.plugin = plugin;
        this.orientate = orientate;
        this.iterations = iterations;
        this.delay = delay;
        this.period = period;
        this.async = async;
    }
    
    protected Result<Void> schedule(Task task) {
        if (async) {
            task.runTaskTimerAsynchronously(plugin, delay, period);
            
        } else {
            task.runTaskTimer(plugin, delay, period);
        }
        
        return task;
    }
    
    
    public static abstract class Builder<GenericExecutor extends EffectExecutor, GenericBuilder extends Builder> {

        protected GenericExecutor executor;

        
        public Builder(GenericExecutor executor) {
            this.executor = executor;
        }
        
        
        protected abstract GenericBuilder getThis();
        
        
        public GenericBuilder orientate(boolean orientate) {
            executor.orientate = orientate;
            return getThis();
        }
        
        public GenericBuilder async(boolean async) {
            executor.async = async;
            return getThis();
        }
        
        public GenericBuilder infinite() {
            executor.iterations = ScheduledResultTask.INFINITE;
            return getThis();
        }
        
        public GenericBuilder iterations(long iterations) {
            executor.iterations = iterations;
            return getThis();
        }
        
        public GenericBuilder delay(long delay) {
            executor.delay = delay;
            return getThis();
        }
        
        public GenericBuilder period(long period) {
            executor.period = period;
            return getThis();
        }
        
        public GenericExecutor build() {
            return executor;
        }

    }
    
}
