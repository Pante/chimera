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

import com.karuslabs.commons.locale.*;

import java.util.Locale;
import java.util.function.Function;
import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static java.util.Locale.getDefault;
import static org.bukkit.ChatColor.translateAlternateColorCodes;


public class Context implements Translatable {
    
    private CommandSender sender;
    private @Nullable Player player;
    private Locale locale;
    
    private String label;
    private @Nullable Command parent;
    private Command command;
    private MessageTranslation translation;
    
    
    public Context(CommandSender sender, String label, @Nullable Command parent, Command command) {
        this(sender, sender instanceof Player ? Locales.getOrDefault(((Player) sender).getLocale(), getDefault()) : getDefault(), label, parent, command, command.getTranslation());
    }
    
    public Context(CommandSender sender, Locale locale, String label, @Nullable Command parent, Command command, MessageTranslation translation) {
        this.sender = sender;
        this.player = sender instanceof Player ? (Player) sender : null;
        this.locale = locale;
        this.label = label;
        this.parent = parent;
        this.command = command;
        this.translation = translation;
    }
    
    
    public void update(String label, Command command) {
        this.label = label;
        this.parent = this.command;
        this.command = command;
        this.translation = command.getTranslation();
    }
    
    
    public void sendColouredSource(String key, Object... arguments) {
        sender.sendMessage(translateAlternateColorCodes('&', translation.locale(locale).format(key, arguments)));
    }  
    
    public void sendFormattedSource(String key, Function<String, String> format, Object... arguments) {
        sender.sendMessage(format.apply(translation.locale(locale).format(key, arguments)));
    }
    
    public void sendSource(String key, Object... arguments) {
        sender.sendMessage(translation.locale(locale).format(key, arguments));
    }
    
    
    public CommandSender getSender() {
        return sender;
    }
    
    public @Nullable Player getPlayer() {
        return player;
    }
        
    public boolean isPlayer() {
        return player != null;
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
    
    @Override
    public MessageTranslation getTranslation() {
        return translation;
    }
    
}
