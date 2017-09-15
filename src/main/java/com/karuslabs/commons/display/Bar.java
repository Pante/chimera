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

import com.google.common.cache.*;

import com.karuslabs.commons.locale.Translation;

import java.util.*;
import java.util.function.Consumer;

import org.bukkit.entity.Player;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;


public abstract class Bar<GenericTask extends Task> {
    
    protected Cache<Player, GenericTask> tasks;
    protected Translation translation;
    protected Consumer<GenericTask> prerender;
    protected Consumer<GenericTask> cancellation;
    protected long frames;
    protected long delay;
    protected long period;
    
    
    public Bar(Translation translation, Consumer<GenericTask> prerender, Consumer<GenericTask> cancellation, long frames, long delay, long period) {
        this.tasks = CacheBuilder.newBuilder().weakKeys().weakValues().build();
        this.translation = translation;
        this.prerender = prerender;
        this.cancellation = cancellation;
        this.frames = frames;
        this.delay = delay;
        this.period = period;
    }
    
    
    public void render(Player player, boolean override) {
        render(asList(player), override);
    }
    
    public void render(Collection<Player> players, boolean override) {
        if (override) {
            players.forEach(this::cancel);
            
        } else {
            players = players.stream().filter(player -> tasks.getIfPresent(player) == null).collect(toList());
        }
        
        if (!players.isEmpty()) {
            render(players);
        }
    }
    
    protected abstract void render(Collection<Player> player);
    
    
    public void cancel(Player player) {
        GenericTask task = tasks.getIfPresent(player);
        if (task != null) {
            task.cancel(player);
        }
    }
    
    
    public Translation getTranslation() {
        return translation;
    }
    
    public long getFrames() {
        return frames;
    }

    public long getDelay() {
        return delay;
    }

    public long getPeriod() {
        return period;
    }
    
    
    public static abstract class Builder<GenericBuilder extends Builder, GenericBar extends Bar> {
        
        protected GenericBar bar;
        
        
        public Builder(GenericBar bar) {
            this.bar = bar;
        }
        
        
        public GenericBuilder translation(Translation translation) {
            bar.translation = translation;
            return getThis();
        }
        
        public GenericBuilder frames(long frames) {
            bar.frames = frames;
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
        
        
        public GenericBar build() {
            return bar;
        }
        
        
        protected abstract GenericBuilder getThis();
        
    }
    
}
