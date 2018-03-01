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
import com.karuslabs.commons.util.concurrent.ScheduledResultTask;

import java.util.Collection;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


class StubBar extends Bar {

    ScheduledResultTask<Void> task;
    
    
    StubBar(Plugin plugin, ScheduledResultTask<Void> task, Translation translation, long iterations, long delay, long period) {
        super(plugin, translation, iterations, delay, period);
        this.task = task;
    }

    @Override
    protected ScheduledResultTask<Void> newTask(Collection<Player> players) {
        return task;
    }
    
    
    static StubBuilder builder(Plugin plugin, ScheduledResultTask<Void> task) {
        return new StubBuilder(new StubBar(plugin, task, null, 0, 0, 0));
    }
    
    
    static class StubBuilder extends Builder<StubBuilder, StubBar> {

        StubBuilder(StubBar bar) {
            super(bar);
        }

        @Override
        protected StubBuilder getThis() {
            return this;
        }
        
    }
    
}
