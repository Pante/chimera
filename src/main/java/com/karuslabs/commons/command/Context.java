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


/**
 * Represents the context in which a {@code Command} is executed.
 */
public class Context implements Translatable {
    
    private CommandSender sender;
    private @Nullable Player player;
    private Locale locale;
    
    private String label;
    private @Nullable Command parent;
    private Command command;
    private MessageTranslation translation;
    
    
    /**
     * Constructs a {@code Context} with the specified {@code Commandsender}, label, parent {@code Command} and {@code Command} which is executed.
     * 
     * @param sender the CommandSender
     * @param label the label
     * @param parent the parent of the Command which is executed
     * @param command the Command which is executed
     */
    public Context(CommandSender sender, String label, @Nullable Command parent, Command command) {
        this(sender, sender instanceof Player ? Locales.getOrDefault(((Player) sender).getLocale(), getDefault()) : getDefault(), label, parent, command, command.getTranslation());
    }
    
    /**
     * Constructs a {@code Context} with the specified {@code CommandSender}, locale of the {@code CommandSender}, label, parent {@code Command},
     * {@code Command} which is executed and the {@code MessageTranslation}.
     * 
     * @param sender the CommandSender
     * @param locale the locale of the CommandSender
     * @param label the label of the command used
     * @param parent the parent of the Command which is executed
     * @param command the Command which is executed
     * @param translation the MessageTranslation of the Command executed
     */
    public Context(CommandSender sender, Locale locale, String label, @Nullable Command parent, Command command, MessageTranslation translation) {
        this.sender = sender;
        this.player = sender instanceof Player ? (Player) sender : null;
        this.locale = locale;
        this.label = label;
        this.parent = parent;
        this.command = command;
        this.translation = translation;
    }
    
    
    /**
     * Updates this {@code Context} with the specified label and {@code Command},
     * setting the current {@code Command} as the parent {@code Command}.
     * 
     * @param label the label
     * @param command the Command
     */
    public void update(String label, Command command) {
        this.label = label;
        this.parent = this.command;
        this.command = command;
        this.translation = command.getTranslation();
    }
    
    
    /**
     * Translates and sends the message associated with the specified key with the specified arguments to the {@code CommandSender}
     * using the {@code MessageTranslation} and locale of the {@code CommandSender}.
     * 
     * @param key the key
     * @param arguments the arguments
     */
    public void sendSource(String key, Object... arguments) {
        sender.sendMessage(translation.locale(locale).format(key, arguments));
    }

    /**
     * Translates and sends the message associated with the specified key with the specified arguments to the {@code CommandSender}, 
     * translating the colour codes prefixed with '{@literal &}' and using the {@code MessageTranslation} and locale of the {@code CommandSender}.
     * 
     * @param key the key
     * @param arguments the arguments
     */    
    public void sendColouredSource(String key, Object... arguments) {
        sender.sendMessage(translateAlternateColorCodes('&', translation.locale(locale).format(key, arguments)));
    }  
    
    /**
     * Translates and sends the message associated with the specified key with the specified arguments to the {@code CommandSender}, 
     * formatting the translated message using the specified {@code Function}, {@code MessageTranslation} and locale of the 
     * {@code CommandSender}.
     * 
     * @param key the key
     * @param format the format to apply
     * @param arguments the arguments
     */
    public void sendFormattedSource(String key, Function<String, String> format, Object... arguments) {
        sender.sendMessage(format.apply(translation.locale(locale).format(key, arguments)));
    }

    
    /**
     * Returns the {@code CommandSender}.
     * 
     * @return the CommandSender
     */
    public CommandSender getSender() {
        return sender;
    }
    
    /**
     * Returns the {@code CommandSender} if the {@code CommandSender} is a {@code Player}, or {@code null}
     * if the {@code CommandSender} is not a {@code Player}.
     * 
     * @return the Player if the CommandSender is a Player; else null
     */
    public @Nullable Player getPlayer() {
        return player;
    }
    
    /**
     * Returns {@code true} if the {@code CommandSender} is a {@code Player}; else {@code false}.
     * 
     * @return true if the CommandSender is a Player; else false
     */
    public boolean isPlayer() {
        return player != null;
    }
    
    /**
     * Returns the {@code Locale} of the {@code CommandSender}.
     * 
     * @return the Locale
     */
    public Locale getLocale() {
        return locale;
    }
    
    /**
     * Sets the {@code locale}.
     * 
     * @param locale the Locale
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }
    
    /**
     * Returns the label of the {@code Command} used.
     * 
     * @return the label
     */
    public String getLabel() {
        return label;
    }
    
    /**
     * Returns the parent of the {@code Command} which is executed, or {@code null} if the {@code Command} has no parent. 
     * 
     * @return the parent Command if the Command executed has a parent; else null
     */
    public @Nullable Command getParentCommand() {
        return parent;
    }
    
    /**
     * Returns the {@code Command} which is executed.
     * 
     * @return the Command
     */
    public Command getCommand() {
        return command;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public MessageTranslation getTranslation() {
        return translation;
    }
    
}
