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

import java.util.*;
import java.util.function.BiFunction;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static java.util.Arrays.asList;
import static java.util.Collections.newSetFromMap;


public class ActionBar {
    
    private static final BiFunction<Player, Task, String> TEXT = (player, task) -> "";
    
    private Plugin plugin;
    private BiFunction<Player, Task, String> function;
    private Translation translation;
    private long frames;
    private long delay;
    private long period;
    
    
    public ActionBar(Plugin plugin, BiFunction<Player, Task, String> function, Translation translation, long frames) {
        this(plugin, function, translation, frames, 0, 0);
    }
    
    public ActionBar(Plugin plugin, BiFunction<Player, Task, String> function, Translation translation, long frames, long delay, long period) {
        this.plugin = plugin;
        this.function = function;
        this.translation = translation;
        this.frames = frames;
        this.delay = delay;
        this.period = period;
    }
    
    
    public void render(Player... players) {
        render(asList(players));
    }
    
    public void render(Collection<Player> players) {
        Set<Player> targets = newSetFromMap(new WeakHashMap<>());
        targets.addAll(players);
        new Task(targets, function, translation.copy(), frames).runTaskTimerAsynchronously(plugin, delay, period);
    }
    
    
    public Plugin getPlugin() {
        return plugin;
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
    
    
    public static Builder builder(Plugin plugin) {
        return new Builder(plugin);
    }
    
    
    public static class Builder {
        
        private ActionBar bar;
        
        
        protected Builder(Plugin plugin) {
            bar = new ActionBar(plugin, TEXT, Translation.NONE, 0);
        }
        
        
        public Builder function(BiFunction<Player, Task, String> function) {
            bar.function = function;
            return this;
        }
        
        public Builder translation(Translation translation) {
            bar.translation = translation;
            return this;
        }
        
        public Builder frames(long frames) {
            bar.frames = frames;
            return this;
        }
        
        public Builder delay(long delay) {
            bar.delay = delay;
            return this;
        }
        
        public Builder period(long period) {
            bar.period = period;
            return this;
        }
        
        public ActionBar build() {
            return bar;
        }
        
    }
    
}
