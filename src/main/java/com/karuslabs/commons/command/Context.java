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
import com.karuslabs.commons.locale.Translation;

import java.util.Locale;
import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Context {
    
    private CommandSender sender;
    private Locale locale;
    
    private String label;
    private Command parent;
    private Command command;
    private Translation translation;
    
    
    public Context(CommandSender sender, String label, @Nullable Command parent, Command command) {
        this(sender, sender instanceof Player ? Locales.get(((Player) sender).getLocale()) : Locale.getDefault(), label, parent, command);
    }
    
    public Context(CommandSender sender, Locale locale, String label, @Nullable Command parent, Command command) {
        this.sender = sender;
        this.locale = locale;
        this.label = label;
        this.parent = parent;
        this.command = command;
        this.translation = null;
    }
    
    
    public void update(String label, Command command) {
        this.label = label;
        this.parent = this.command;
        this.command = command;
        this.translation = null;
    }
    
    
    public CommandSender getSender() {
        return sender;
    }
    
    public @Nullable Player getPlayer() {
        return isPlayer() ? (Player) sender : null;
    }
        
    public boolean isPlayer() {
        return sender instanceof Player;
    }
    
    public Locale getLocale() {
        return locale;
    }
    
    public void setLocale(Locale locale) {
        this.locale = locale;
    }
    
    
    public String getLabel() {
        return label;
    }
    
    public @Nullable Command getParentCommand() {
        return parent;
    }
    
    public Command getCommand() {
        return command;
    }
    
    public Translation getTranslation() {
        if (translation == null) {
            translation = command.getTranslations().get(locale);
        }
        
        return translation;
    }
    
}
