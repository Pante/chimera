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

import java.util.*;
import java.util.function.*;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class TitleBar extends Bar<Set<Player>> {
    
    private static final BiConsumer<TitleBarTask, Player> CONSUMER = (task, player) -> {};
    
    private BiConsumer<TitleBarTask, Player> consumer;
    private int fadeIn;
    private int stay;
    private int fadeOut;
    
    
    public TitleBar(Plugin plugin, Translation translation, BiConsumer<TitleBarTask, Player> consumer, long frames, long delay, long period, int fadeIn, int stay, int fadeOut) {
        super(plugin, translation, frames, delay, period);
        this.consumer = consumer;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    
    @Override
    public Promise<Set<Player>> render(Collection<Player> players) {
        TitleBarTask task = new TitleBarTask(frames, translation, consumer, fadeIn, stay, fadeOut);
        task.runTaskTimerAsynchronously(plugin, delay, period);
        
        return task;
    }

    
    public int getFadeIn() {
        return fadeIn;
    }

    public int getStay() {
        return stay;
    }

    public int getFadeOut() {
        return fadeOut;
    }
    
    
    public static TitleBarBuilder builder(Plugin plugin) {
        return new TitleBarBuilder(new TitleBar(plugin, Translation.NONE, CONSUMER, 0, 0, 0, 0, 0, 0));
    }
    
    
    public static class TitleBarBuilder extends Builder<TitleBarBuilder, TitleBar> {

        protected TitleBarBuilder(TitleBar bar) {
            super(bar);
        }
        
        
        public TitleBarBuilder consumer(BiConsumer<TitleBarTask, Player> consumer) {
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
        protected TitleBarBuilder getThis() {
            return this;
        }
        
    }
    
}
