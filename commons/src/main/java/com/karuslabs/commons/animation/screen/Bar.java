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


public abstract class Bar {
    
    public static final long INFINITE = -1;
    
    
    protected Plugin plugin;
    protected Translation translation;
    protected long iterations;
    protected long delay;
    protected long period;
    
    
    public Bar(Plugin plugin, Translation translation, long iterations, long delay, long period) {
        this.plugin = plugin;
        this.translation = translation;
        this.iterations = iterations;
        this.delay = delay;
        this.period = period;
    }
    
    
    public @Nonnull Result<Void> render(Player... player) {
        return render(asList(player));
    }
    
    public @Nonnull Result<Void> render(Collection<Player> players) {
        ScheduledResultTask<Void> task = newTask(players);
        task.runTaskTimerAsynchronously(plugin, delay, period);
        return task;
    }
    
    protected abstract @Nonnull ScheduledResultTask<Void> newTask(Collection<Player> players);
    
    
    public static abstract class Builder<GenericBuilder extends Builder, GenericBar extends Bar> {
        
        protected GenericBar bar;
        
        
        public Builder(GenericBar bar) {
            this.bar = bar;
        }
        
        
        public GenericBuilder translation(Translation translation) {
            bar.translation = translation;
            return getThis();
        }
        
        public GenericBuilder infinite() {
            bar.iterations = INFINITE;
            return getThis();
        }
        
        public GenericBuilder iterations(long iterations) {
            bar.iterations = iterations;
            return getThis();
        }
        
        public GenericBuilder delay(long delay) {
            bar.delay = delay;
            return getThis();
        }
        
        public GenericBuilder period(long period) {
            bar.period = period;
            return getThis();
        }

        protected abstract @Nonnull GenericBuilder getThis();
        
        public GenericBar build() {
            return bar;
        }
        
    } 
    
}
