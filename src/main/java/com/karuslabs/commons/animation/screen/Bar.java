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
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;

import org.bukkit.entity.Player;

import static java.util.Collections.singletonList;


public abstract class Bar {
    
    public static final long INFINITE = -1;
    
    
    private ScheduledExecutor executor;
    protected Translation translation;
    protected long iterations;
    protected long delay;
    protected long period;
    protected TimeUnit unit;
    
    
    public Bar(ScheduledExecutor executor, Translation translation, long iterations, long delay, long period, TimeUnit unit) {
        this.executor = executor;
        this.translation = translation;
        this.iterations = iterations;
        this.delay = delay;
        this.period = period;
        this.unit = unit;
    }
    
    
    public ScheduledPromise<?> render(Player player) {
        return render(singletonList(player));
    }
    
    public ScheduledPromise<?> render(Collection<Player> players) {
        return ScheduledPromise.of(executor.scheduleWithFixedDelay(runnable(players), delay, period, unit));
    }
    
    protected abstract @Nonnull ScheduledRunnable runnable(Collection<Player> players);
    
    
    public static abstract class Builder<GenericBuilder extends Builder, GenericBar extends Bar> {
        
        protected GenericBar bar;
        
        
        public Builder(GenericBar bar) {
            this.bar = bar;
        }
        
        
        public GenericBuilder translation(Translation translation) {
            bar.translation = translation;
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
        
        public GenericBuilder unit(TimeUnit unit) {
            bar.unit = unit;
            return getThis();
        }
        
                
        protected abstract @Nonnull GenericBuilder getThis();
        
        
        public GenericBar build() {
            return bar;
        }
        
    } 
    
}
