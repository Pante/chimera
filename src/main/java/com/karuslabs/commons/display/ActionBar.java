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

import java.text.MessageFormat;
import java.util.List;
import java.util.function.BiFunction;

import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import static java.util.Arrays.asList;
import static net.md_5.bungee.api.ChatMessageType.ACTION_BAR;


public class ActionBar {
    
    private Plugin plugin;
    private BiFunction<Player, Long, Object> function;
    private MessageFormat format;
    private long frames;
    private long delay;
    private long period;
    
    
    public ActionBar(Plugin plugin, BiFunction<Player, Long, Object> function, MessageFormat format, long frames, long delay, long period) {
        this.plugin = plugin;
        this.function = function;
        this.format = format;
        this.frames = frames;
        this.delay = delay;
        this.period = period;
    }
    
    
    public void render(Player... players) {
        render(asList(players));
    }
    
    public void render(List<Player> players) {
        new Task(players, function, format, frames).runTaskTimerAsynchronously(plugin, delay, period);
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
    
    
    public static class Task extends BukkitRunnable {
        
        private List<Player> players;
        private BiFunction<Player, Long, Object> function;
        private MessageFormat format;
        private long frames;
        
        
        public Task(List<Player> players, BiFunction<Player, Long, Object> function, MessageFormat format, long frames) {
            this.players = players;
            this.function = function;
            this.format = format;
            this.frames = frames;
        }
        
        
        @Override
        public void run() {
            if (0 < frames--) {
                players.forEach(player -> player.spigot().sendMessage(ACTION_BAR, new TextComponent(format.format(function.apply(player, frames)))));
                
            } else {
                cancel();
            }
        }
        
    }
    
}
