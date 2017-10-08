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
import java.util.function.*;
import javax.annotation.Nonnull;

import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static com.karuslabs.commons.locale.MessageTranslation.NONE;


public class SharedProgressBar extends AbstractBar {
    
    private Supplier<BiConsumer<BossBar, Context>> consumer;
    
    
    public SharedProgressBar(Plugin plugin, Translation translation, Supplier<BossBar> supplier, Supplier<BiConsumer<BossBar, Context>> consumer, long iterations, long delay, long period) {
        super(plugin, translation, supplier, iterations, delay, period);
        this.consumer = consumer;
    }
    

    @Override
    protected @Nonnull ScheduledPromiseTask<?> newTask(Collection<Player> players) {
        BossBar bar = supplier.get();
        players.forEach(bar::addPlayer);
        
        return new ScheduledTask(bar, consumer.get(), translation, iterations);
    }
    
    
    static class ScheduledTask extends Task {
        
        private BossBar bar;
        private BiConsumer<BossBar, Context> consumer;
        
        
        ScheduledTask(BossBar bar, BiConsumer<BossBar, Context> consumer, Translation translation, long iterations) {
            super(translation, iterations);
            this.bar = bar;
            this.consumer = consumer;
        }

        @Override
        protected void process() {
            consumer.accept(bar, this);
        }
        
        @Override
        protected void callback() {
            bar.removeAll();
        }
        
    }
    
    
    public static SharedProgressBarBuilder builder(Plugin plugin) {
        return new SharedProgressBarBuilder(new SharedProgressBar(plugin, NONE, null, null, 0, 0, 0));
    }
    
    public static class SharedProgressBarBuilder extends AbstractBuilder<SharedProgressBarBuilder, SharedProgressBar> {

        public SharedProgressBarBuilder(SharedProgressBar bar) {
            super(bar);
        }
        
        
        public SharedProgressBarBuilder consumer(Supplier<BiConsumer<BossBar, Context>> consumer) {
            bar.consumer = consumer;
            return this;
        }
        
        
        @Override
        protected @Nonnull SharedProgressBarBuilder getThis() {
            return this;
        }
    
    }
    
}
