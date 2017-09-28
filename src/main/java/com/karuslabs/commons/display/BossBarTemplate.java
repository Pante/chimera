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

import com.karuslabs.commons.util.Template;

import javax.annotation.Nonnull;

import org.bukkit.Server;
import org.bukkit.boss.*;


public class BossBarTemplate implements Template<BossBar> {

    public static final BarFlag[] FLAGS = new BarFlag[] {};
    
    private Server server;
    private String message;
    private BarColor color;
    private BarStyle style;
    private BarFlag[] flags;
    private double progress;

    
    public BossBarTemplate(Server server, String message, BarColor color, BarStyle style) {
        this(server, message, color, style, FLAGS, 1.0);
    }
    
    public BossBarTemplate(Server server, String message, BarColor color, BarStyle style, BarFlag[] flags, double progress) {
        this.server = server;
        this.message = message;
        this.color = color;
        this.style = style;
        this.flags = flags;
        this.progress = progress;
    }
    
    
    @Override
    public @Nonnull BossBar create() {
        BossBar bar = server.createBossBar(message, color, style, flags);
        bar.setProgress(progress);
        return bar;
    }
    

    public Server getServer() {
        return server;
    }

    
    public void setMessage(String message) {
        this.message = message;
    }

    public void setColor(BarColor color) {
        this.color = color;
    }

    public void setStyle(BarStyle style) {
        this.style = style;
    }

    public void setFlags(BarFlag[] flags) {
        this.flags = flags;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

}
