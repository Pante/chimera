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
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import java.util.function.Supplier;

import org.checkerframework.checker.nullness.qual.Nullable;


public class Continual<T> extends EventualTask<T> implements RunnableScheduledFuture<T> {    
    
    public static final long INFINITE = -1;
    
    private @Nullable Supplier<T> supplier;
    private @Nullable T result;
    private long times;
    private long elapsed;
    private long period;
    
    
    public Continual(Executable<T> executable, long elapsed, long period) {
        this(executable, INFINITE, elapsed, period);
    }
    
    public Continual(Executable<T> executable, long times, long elapsed, long period) {
        super(executable, null);
        this.times = times;
        this.elapsed = elapsed;
        this.period = period;
    }
    
    
    public Continual(Runnable runnable, Supplier<T> supplier, long elapsed, long period) {
        this(runnable, supplier, INFINITE, elapsed, period);
    }
    
    public Continual(Runnable runnable, Supplier<T> supplier, long times, long elapsed, long period) {
        super(runnable, null);
        this.supplier = supplier;
        this.times = times;
        this.elapsed = elapsed;
        this.period = period;
    }
    
    
    public Continual(Runnable runnable, T result, long elapsed, long period) {
        this(runnable, result, INFINITE, elapsed, period);
    } 
    
    public Continual(Runnable runnable, T result, long times, long elapsed, long period) {
        super(runnable, null);
        this.result = result;
        this.times = times;
        this.elapsed = elapsed;
        this.period = period;
    }
    
    
    @Override
    public void run() {
        if (times == 0 || (runAndReset() && times > 0 && --times == 0)) {
            if (supplier != null) {
                set(supplier.get());
                
            } else if (result != null) {
                set(result);
            }
            
        } else {
            elapsed += period;
        }
    }


    @Override
    public int compareTo(Delayed other) {
        if (other == this) {
            return 0;
        }
        
        if (other instanceof Continual) {
            var x = (Continual) other;
            long diff = elapsed - x.elapsed;
            if (diff < 0) {
                return -1;
                
            } else {
                return 1;
            }
        }

        long diff = getDelay(NANOSECONDS) - other.getDelay(NANOSECONDS);
        return (diff < 0) ? -1 : (diff > 0) ? 1 : 0;
    }
    
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(elapsed - System.nanoTime(), NANOSECONDS);
    }
    
    @Override
    public boolean isPeriodic() {
        return true;
    }
    
    public long times() {
        return times;
    }
    
}
