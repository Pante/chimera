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
import java.util.function.*;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static com.karuslabs.commons.display.ActionBarTask.NONE;
import static java.util.Collections.newSetFromMap;


public class ActionBar extends Bar<ActionBarTask> {
    
    private static final BiFunction<Player, ActionBarTask, String> FUNCTION = (player, task) -> "";
    
    
    private Plugin plugin;
    private BiFunction<Player, ActionBarTask, String> function;
    
    
    public ActionBar(Plugin plugin, BiFunction<Player, ActionBarTask, String> function, Translation translation, long frames, long delay, long period) {
        this(plugin, function, translation, NONE, NONE, frames, delay, period);
    }
    
    public ActionBar(Plugin plugin, BiFunction<Player, ActionBarTask, String> function, Translation translation, Consumer<ActionBarTask> prerender, Consumer<ActionBarTask> cancellation, long frames, long delay, long period) {
        super(translation, prerender, cancellation, frames, delay, period);
        this.plugin = plugin;
        this.function = function;
    }

    
    @Override
    protected void render(Collection<Player> players) {
        Set<Player> targets = newSetFromMap(new WeakHashMap<>());
        targets.addAll(players);
        
        ActionBarTask task = new ActionBarTask(targets, function, translation, prerender, cancellation, frames);
        targets.forEach(player -> tasks.put(player, task));
        
        task.runTaskTimerAsynchronously(plugin, delay, period);
    }
    
    
    public static ActionBarBuilder builder(Plugin plugin) {
        return new ActionBarBuilder(new ActionBar(plugin, FUNCTION, Translation.NONE, 0, 0, 0));
    }
    
    
    public static class ActionBarBuilder extends Builder<ActionBarBuilder, ActionBar> {

        public ActionBarBuilder(ActionBar bar) {
            super(bar);
        }

        
        public ActionBarBuilder function(BiFunction<Player, ActionBarTask, String> function) {
            bar.function = function;
            return this;
        }
        
        
        @Override
        protected ActionBarBuilder getThis() {
            return this;
        }
        
    }
    
}
