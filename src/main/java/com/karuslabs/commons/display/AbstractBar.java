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

import java.util.function.BiConsumer;

import org.bukkit.Server;
import org.bukkit.boss.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public abstract class AbstractBar<T, GenericTask extends BarTask> extends Bar<T> {
    
    public static final BarFlag[] FLAGS = new BarFlag[] {};
    
    
    private Server server;
    protected BiConsumer<GenericTask, BossBar> consumer;
    protected BarColor color;
    protected BarStyle style;
    protected BarFlag[] flags;
    protected double progress;

    
    public AbstractBar(Plugin plugin, Translation translation, BiConsumer<GenericTask, BossBar> consumer, BarColor color, BarStyle style, BarFlag[] flags, double progress, long frames, long delay, long period) {
        super(plugin, translation, frames, delay, period);
        this.server = plugin.getServer();
        this.consumer = consumer;
        this.color = color;
        this.style = style;
        this.flags = flags;
        this.progress = progress;
    }
    
    
    public BossBar create(Player player) {
        BossBar bar = create();
        bar.addPlayer(player);
        
        return bar;
    }
    
    public BossBar create() {
        BossBar bar = server.createBossBar("", color, style, flags);
        bar.setProgress(progress);
        
        return bar;
    }

    
    public BarColor getColor() {
        return color;
    }

    public BarStyle getStyle() {
        return style;
    }

    public BarFlag[] getFlags() {
        return flags;
    }
    
    public double getProgress() {
        return progress;
    }
    
    
    public static abstract class AbstractBuilder<GenericBuilder extends AbstractBuilder, GenericBar extends AbstractBar, GenericTask extends BarTask> extends Builder<GenericBuilder, GenericBar> {
        
        public AbstractBuilder(GenericBar bar) {
            super(bar);
        }
        
        
        public GenericBuilder consumer(BiConsumer<GenericTask, BossBar> consumer) {
            bar.consumer = consumer;
            return getThis();
        }
        
        public GenericBuilder color(BarColor color) {
            bar.color = color;
            return getThis();
        }
        
        public GenericBuilder style(BarStyle style) {
            bar.style = style;
            return getThis();
        }
        
        public GenericBuilder flags(BarFlag[] flags) {
            bar.flags = flags;
            return getThis();
        }
        
        public GenericBuilder progress(double progress) {
            bar.progress = progress;
            return getThis();
        }
        
    }

}
