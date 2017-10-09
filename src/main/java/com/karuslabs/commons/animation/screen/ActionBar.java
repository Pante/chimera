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
import javax.annotation.*;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static com.karuslabs.commons.collection.Sets.weakSet;
import static com.karuslabs.commons.locale.MessageTranslation.NONE;
import static net.md_5.bungee.api.ChatMessageType.ACTION_BAR;
import static net.md_5.bungee.api.chat.TextComponent.fromLegacyText;


public class ActionBar extends Bar {
    
    private Supplier<BiFunction<Player, Context, String>> supplier;

    
    public ActionBar(Plugin plugin, Translation translation, Supplier<BiFunction<Player, Context, String>> supplier, long iterations, long delay, long period) {
        super(plugin, translation, iterations, delay, period);
        this.supplier = supplier;
    }

    
    @Override
    protected @Nonnull ScheduledPromiseTask<?> newTask(Collection<Player> players) {
        return new ScheduledTask(weakSet(players), supplier.get(), translation, iterations);
    }
    
    
    static class ScheduledTask extends Task {
        
        private Set<Player> players;
        private BiFunction<Player, Context, String> function;
        
        public ScheduledTask(Set<Player> players, BiFunction<Player, Context, String> function, Translation translation, long iterations) {
            super(translation, iterations);
            this.players = players;
            this.function = function;
        }
        
        @Override
        protected void process() {
            players.forEach(player -> player.spigot().sendMessage(ACTION_BAR, fromLegacyText(function.apply(player, this))));
        }
        
    }
    
    
    public static ActionBarBuilder builder(Plugin plugin) {
        return new ActionBarBuilder(new ActionBar(plugin, NONE, null, 0, 0, 0));
    }
    
    public static class ActionBarBuilder extends Builder<ActionBarBuilder, ActionBar> {

        protected ActionBarBuilder(ActionBar bar) {
            super(bar);
        }
        
        public ActionBarBuilder function(Supplier<BiFunction<Player, Context, String>> supplier) {
            bar.supplier = supplier;
            return this;
        }
        
        @Override
        protected @Nonnull ActionBarBuilder getThis() {
            return this;
        }
        
    }
    
}