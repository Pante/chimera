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


/**
 * A concrete subclass of {@code Bar} which represents the individual boss bars for {@code Player}s.
 */
public class ProgressBar extends AbstractBar {
    
    private Supplier<BiConsumer<BossBar, Context>> consumer;

    
    /**
     * Constructs a {@code ProgressBar} with the specified plugin, translation, {@code BossBar} supplier, {@code BiConsumer} supplier, iterations, delay and period.
     * 
     * @param plugin the plugin
     * @param translation the translation
     * @param supplier the BossBar supplier
     * @param consumer the BiConsumer supplier
     * @param iterations the number of iterations
     * @param delay the ticks to wait before starting to render the bar
     * @param period the ticks to wait between runs
     */
    public ProgressBar(Plugin plugin, Translation translation, Supplier<BossBar> supplier, Supplier<BiConsumer<BossBar, Context>> consumer, long iterations, long delay, long period) {
        super(plugin, translation, supplier, iterations, delay, period);
        this.consumer = consumer;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    protected @Nonnull ScheduledResultTask<Void> newTask(Collection<Player> players) {
        List<BossBar> bars = players.stream().map(player -> {
            BossBar bar = supplier.get();
            bar.addPlayer(player);
            return bar;
        }).collect(toList());
        
        return new ScheduledTask(bars, consumer.get(), translation, iterations);
    }
    
    
    static class ScheduledTask extends Task {
        
        private List<BossBar> bars;
        private BiConsumer<BossBar, Context> consumer;
        
        
        ScheduledTask(List<BossBar> bars, BiConsumer<BossBar, Context> consumer, Translation translation, long iterations) {
            super(translation, iterations);
            this.bars = bars;
            this.consumer = consumer;
        }

        
        @Override
        protected void process() {
            for (BossBar bar : bars) {
                consumer.accept(bar, this);
            }
        }
        
        @Override
        protected void callback() {
            bars.forEach(BossBar::removeAll);
        }
        
    }
    
    
    /**
     * Creates a {@code ProgressBar} builder with the specified plugin.
     * 
     * @param plugin the plugin
     * @return the builder
     */
    public static ProgressBarBuilder builder(Plugin plugin) {
        return new ProgressBarBuilder(new ProgressBar(plugin, NONE, null, null, 0, 0, 0));
    }
    
    
    /**
     * Represents a builder for {@code ProgressBar}s.
     */
    public static class ProgressBarBuilder extends AbstractBuilder<ProgressBarBuilder, ProgressBar>  {

        private ProgressBarBuilder(ProgressBar bar) {
            super(bar);
        }

        
        /**
         * Sets the supplier which supplies the {@code BiConsumer} for rendering the boss bar.
         * 
         * @param consumer the supplier
         * @return this
         */
        public ProgressBarBuilder consumer(Supplier<BiConsumer<BossBar, Context>> consumer) {
            bar.consumer = consumer;
            return this;
        }
        
        
        /**
         * {@inheritDoc}
         */
        @Override
        protected @Nonnull ProgressBarBuilder getThis() {
            return this;
        }
    
    }
    
}
