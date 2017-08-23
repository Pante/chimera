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
package com.karuslabs.commons.command;

import com.karuslabs.commons.locale.Locales;

import java.util.Locale;
import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Source {
    
    private CommandSender sender;
    
    
    public Source(CommandSender sender) {
        this.sender = sender;
    }
    
    
    public CommandSender get() {
        return sender;
    }
    
    
    public boolean isPlayer() {
        return sender instanceof Player;
    }
    
    public @Nullable Player asPlayer() {
        if (isPlayer()) {
            return (Player) sender;
            
        } else {
            return null;
        }
    }
    
    
    public Locale getLocale() {
        if (isPlayer()) {
            return Locales.getOrDefault(((Player) sender).getLocale(), Locale.getDefault());
            
        } else {
            return Locale.getDefault();
        }
    }
    
}
