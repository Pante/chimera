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

import java.util.*;
import java.util.function.*;
import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static com.karuslabs.commons.collection.Sets.weakSet;
import static com.karuslabs.commons.locale.MessageTranslation.NONE;


public class TitleBar extends Bar {
    
    private Supplier<BiConsumer<Player, TitleContext>> consumer;
    private int fadeIn;
    private int stay;
    private int fadeOut;
    
    
    public TitleBar(Plugin plugin, Translation translation, Supplier<BiConsumer<Player, TitleContext>> consumer, int fadeIn, int stay, int fadeOut, long iterations, long delay, long period) {
        super(plugin, translation, iterations, delay, period);
    }

    
    @Override
    protected @Nonnull ScheduledPromiseTask<?> newTask(Collection<Player> players) {
        return new ScheduledTask(weakSet(players), consumer.get(), translation, fadeIn, stay, fadeOut, iterations);
    }
    
    
    static class ScheduledTask extends Task implements TitleContext {
        
        private Set<Player> players;
        private BiConsumer<Player, TitleContext> consumer;
        private int fadeIn;
        private int stay;
        private int fadeOut;
        
        
        ScheduledTask(Set<Player> players, BiConsumer<Player, TitleContext> consumer, Translation translation, int fadeIn, int stay, int fadeOut, long iterations) {
            super(translation, iterations);
            this.players = players;
            this.consumer = consumer;
            this.fadeIn = fadeIn;
            this.stay = stay;
            this.fadeOut = fadeOut;
        }

        
        @Override
        protected void process() {
            players.forEach(player -> consumer.accept(player, this));
        }

        @Override
        public int getFadeIn() {
            return fadeIn;
        }

        @Override
        public int getStay() {
            return stay;
        }

        @Override
        public int getFadeOut() {
            return fadeOut;
        }
        
    }
    
    
    public static TitleBarBuilder builder(Plugin plugin) {
        return new TitleBarBuilder(new TitleBar(plugin, NONE, null, 0, 0, 0, 0, 0, 0));
    }
    
    public static class TitleBarBuilder extends Builder<TitleBarBuilder, TitleBar> {

        public TitleBarBuilder(TitleBar bar) {
            super(bar);
        }

        public TitleBarBuilder consumer(Supplier<BiConsumer<Player, TitleContext>> consumer) {
            bar.consumer = consumer;
            return this;
        }
        
        public TitleBarBuilder fadeIn(int fadeIn) {
            bar.fadeIn = fadeIn;
            return this;
        }
        
        public TitleBarBuilder stay(int stay) {
            bar.stay = stay;
            return this;
        }
        
        public TitleBarBuilder fadeOut(int fadeOut) {
            bar.fadeOut = fadeOut;
            return this;
        }
        
        @Override
        protected @Nonnull TitleBarBuilder getThis() {
            return this;
        }
    
    }
    
}
