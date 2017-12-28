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

import com.karuslabs.commons.locale.Translation;
import com.karuslabs.commons.util.concurrent.*;

import java.util.Collection;
import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static java.util.Arrays.asList;


/**
 * Represents a bar on the screens of {@code Player}s.
 */
public abstract class Bar {
    
    /**
     * Signifies that the rendering task should execute for an infinite number of iterations.
     */
    public static final long INFINITE = -1;
    
    
    /**
     * The owning {@code Plugin}.
     */
    protected Plugin plugin;
    /**
     * The translation.
     */
    protected Translation translation;
    /**
     * The number of iterations to execute the rendering task.
     */
    protected long iterations;
    /**
     * The ticks to wait before running the task for the first time.
     */
    protected long delay;
    /**
     * The ticks to wait between runs.
     */
    protected long period;
    
    
    /**
     * Constructs a {@code Bar} with the specified {@code Plugin}, {@code Translation}, iterations, delay and period.
     * 
     * @param plugin the plugin
     * @param translation the translation
     * @param iterations the number of iterations
     * @param delay the ticks to wait before starting to render the bar
     * @param period the ticks to wait between runs
     */
    public Bar(Plugin plugin, Translation translation, long iterations, long delay, long period) {
        this.plugin = plugin;
        this.translation = translation;
        this.iterations = iterations;
        this.delay = delay;
        this.period = period;
    }
    
    
    /**
     * Asynchronously renders this {@code Bar} to the specified {@code Player}s.
     * 
     * @param player the players
     * @return a Result for the asynchronous rendering
     */
    public @Nonnull Result<Void> render(Player... player) {
        return render(asList(player));
    }
    
    /**
     * Asynchronously renders this {@code Bar} to the specified {@code Player}s.
     * 
     * @param players the players
     * @return a Result for the asynchronous rendering
     */
    public @Nonnull Result<Void> render(Collection<Player> players) {
        ScheduledResultTask<Void> task = newTask(players);
        task.runTaskTimerAsynchronously(plugin, delay, period);
        return task;
    }
    
    /**
     * Creates a {@code ScheduledResultTask} for rendering this {@code Bar} to the specified {@code Player}s.
     * This method is invoked by {@link #render(Collection)} and {@link #render(org.bukkit.entity.Player)}.
     * 
     * Subclasses should implement this method to customise the creation of {@code} ScheduledResultTask}s for rendering
     * this {@code Bar} to the specified {@code Player}s.
     * 
     * @param players the players
     * @return the ScheduledResultTask
     */
    protected abstract @Nonnull ScheduledResultTask<Void> newTask(Collection<Player> players);
    
    
    /**
     * Represents a builder for {@code Bar}s.
     * 
     * @param <GenericBuilder> the type of Builder to return
     * @param <GenericBar> the subclass of Bar
     */
    public static abstract class Builder<GenericBuilder extends Builder, GenericBar extends Bar> {
        
        /**
         * The {@code GenericBar} to build. 
         */
        protected GenericBar bar;
        
        
        /**
         * Constructs a {@code Builder} with the specified {@code GenericBar}.
         * 
         * @param bar the bar
         */
        public Builder(GenericBar bar) {
            this.bar = bar;
        }
        
        
        /**
         * Sets the {@code Translation}.
         * 
         * @param translation the translation
         * @return this
         */
        public GenericBuilder translation(Translation translation) {
            bar.translation = translation;
            return getThis();
        }
        
        /**
         * Sets the bar to execute for an infinite number of iterations.
         * 
         * @return this
         */
        public GenericBuilder infinite() {
            bar.iterations = INFINITE;
            return getThis();
        }
        
        /**
         * Sets the iterations.
         * 
         * @param iterations the iterations
         * @return this
         */
        public GenericBuilder iterations(long iterations) {
            bar.iterations = iterations;
            return getThis();
        }
        
        /**
         * Sets the delay. 
         * 
         * @param delay the delay
         * @return this
         */
        public GenericBuilder delay(long delay) {
            bar.delay = delay;
            return getThis();
        }
        
        /**
         * Sets the period.
         * 
         * @param period the period
         * @return this
         */
        public GenericBuilder period(long period) {
            bar.period = period;
            return getThis();
        }

        /**
         * Returns a generic version of {@code this}.
         *
         * @return this
         */
        protected abstract @Nonnull GenericBuilder getThis();
        
        /**
         * Builds the {@code GenericBar}.
         * 
         * @return the bar
         */
        public GenericBar build() {
            return bar;
        }
        
    } 
    
}
