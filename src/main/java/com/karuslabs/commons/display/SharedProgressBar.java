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
package com.karuslabs.commons.display;

import com.karuslabs.commons.locale.Translation;
import com.karuslabs.commons.util.concurrent.Promise;

import java.util.Collection;
import java.util.function.BiConsumer;

import org.bukkit.boss.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class SharedProgressBar extends AbstractBar<BossBar, SharedProgressBarTask> {
    
    private static final BiConsumer<SharedProgressBarTask, BossBar> CONSUMER = (task, bar) -> {};
    
    
    private BiConsumer<SharedProgressBarTask, BossBar> consumer;

    
    public SharedProgressBar(Plugin plugin, Translation translation, BiConsumer<SharedProgressBarTask, BossBar> consumer, BarColor color, BarStyle style, BarFlag[] flags, double progress, long frames, long delay, long period) {
        super(plugin, translation, consumer, color, style, flags, progress, frames, delay, period);
    }


    
    @Override
    public Promise<BossBar> render(Collection<Player> players) {
        BossBar bar = create();
        players.forEach(bar::addPlayer);
        
        SharedProgressBarTask task = new SharedProgressBarTask(frames, translation, bar, consumer);
        task.runTaskTimerAsynchronously(plugin, delay, period);
        
        return task;
    }
    
    
    public static SharedProgressBarBuilder builder(Plugin plugin) {
        return new SharedProgressBarBuilder(new SharedProgressBar(plugin, Translation.NONE, CONSUMER, BarColor.BLUE, BarStyle.SEGMENTED_10, FLAGS, 0, 0, 0, 0));
    }
    
    
    public static class SharedProgressBarBuilder extends AbstractBuilder<SharedProgressBarBuilder, SharedProgressBar, SharedProgressBarTask> {

        public SharedProgressBarBuilder(SharedProgressBar bar) {
            super(bar);
        }

        @Override
        protected SharedProgressBarBuilder getThis() {
            return this;
        }
        
    }
    
}
