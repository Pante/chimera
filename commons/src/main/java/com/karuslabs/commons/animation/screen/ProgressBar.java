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

import java.util.*;
import java.util.function.*;
import javax.annotation.Nonnull;

import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static com.karuslabs.commons.locale.MessageTranslation.NONE;
import static java.util.stream.Collectors.toList;


public class ProgressBar extends AbstractBar {
    
    private Supplier<BiConsumer<BossBar, Context>> format;

    
    public ProgressBar(Plugin plugin, Translation translation, Supplier<BossBar> supplier, Supplier<BiConsumer<BossBar, Context>> format, long iterations, long delay, long period) {
        super(plugin, translation, supplier, iterations, delay, period);
        this.format = format;
    }

    
    @Override
    protected @Nonnull ScheduledResultTask<Void> newTask(Collection<Player> players) {
        List<BossBar> bars = players.stream().map(player -> {
            BossBar bar = supplier.get();
            bar.addPlayer(player);
            return bar;
        }).collect(toList());
        
        return new ScheduledTask(bars, format.get(), translation, iterations);
    }
    
    
    static class ScheduledTask extends Task {
        
        private List<BossBar> bars;
        private BiConsumer<BossBar, Context> format;
        
        
        ScheduledTask(List<BossBar> bars, BiConsumer<BossBar, Context> format, Translation translation, long iterations) {
            super(translation, iterations);
            this.bars = bars;
            this.format = format;
        }

        
        @Override
        protected void process() {
            for (BossBar bar : bars) {
                format.accept(bar, this);
            }
        }
        
        @Override
        protected void callback() {
            bars.forEach(BossBar::removeAll);
        }
        
    }
    
    
    public static ProgressBarBuilder builder(Plugin plugin) {
        return new ProgressBarBuilder(new ProgressBar(plugin, NONE, null, null, 0, 0, 0));
    }
    
    
    public static class ProgressBarBuilder extends AbstractBuilder<ProgressBarBuilder, ProgressBar>  {

        private ProgressBarBuilder(ProgressBar bar) {
            super(bar);
        }

        
        public ProgressBarBuilder format(Supplier<BiConsumer<BossBar, Context>> format) {
            bar.format = format;
            return this;
        }
        
        
        @Override
        protected @Nonnull ProgressBarBuilder getThis() {
            return this;
        }
    
    }
    
}
