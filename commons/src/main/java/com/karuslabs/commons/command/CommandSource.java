/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
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

import java.util.*;
import java.util.function.*;
import javax.annotation.Nullable;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.*;
import org.bukkit.plugin.Plugin;

import static org.bukkit.ChatColor.translateAlternateColorCodes;


/**
 * Represents a source which may execute commands.
 */
public class CommandSource implements CommandSender, Translatable {
    
    private CommandSender sender;
    private MessageTranslation translation;
    private @Nullable Player player;
    private Locale locale;
    
    
    /**
     * Constructs a {@code CommandSource} with the specified underlying {@code CommandSneder} and {@code MessageTranslation}.
     * 
     * @param sender the underlying CommandSender
     * @param translation the translation
     */
    public CommandSource(CommandSender sender, MessageTranslation translation) {
        this.sender = sender;
        this.translation = translation;
        this.player = (sender instanceof Player) ? (Player) sender : null;
        this.locale = Locales.getOrDefault(player != null ? player.getLocale() : "123");
    }
    
    
    /**
     * Executes the specified consumer if this {@code CommandSource} is a {@code Player}.
     * 
     * This method may be used in conjuction with {@link #orElse(Consumer)}.
     * 
     * @param consumer the consumer to execute, if this CommandSource is a Player
     * @return this
     */
    public CommandSource ifPlayer(Consumer<Player> consumer) {
        if (player != null) {
            consumer.accept(player);
        }
        
        return this;
    }
    
    /**
     * Executes the specified consumer if this {@code CommandSource} is not a {@code Player}.
     * 
     * This method may be used in conjuction with {@link #ifPlayer(Consumer)}. 
     * 
     * @param consumer the consumer to execute, if this CommandSource is not a Player
     */
    public void orElse(Consumer<CommandSource> consumer) {
        if (player == null) {
            consumer.accept(this);
        }
    }
    
    
    /**
     * Sends the translated message associated with the specified key with the specified arguments, 
     * translating the colour codes prefixed with '{@literal &}' in the translated message }.
     * 
     * @param key the key
     * @param arguments the arguments
     */
    public void sendColouredTranslation(String key, Object... arguments) {
        sender.sendMessage(translateAlternateColorCodes('&', translation.locale(locale).format(key, arguments)));
    }
    
    /**
     * Sends the translated message associated with the specified key with the specified arguments, 
     * formatting the translated message using the specified {@code Function}.
     * 
     * @param key the key
     * @param format the format to apply
     * @param arguments the arguments
     */
    public void sendFormattedTranslation(String key, Function<String, String> format, Object... arguments) {
        sender.sendMessage(format.apply(translation.locale(locale).format(key, arguments)));
    }
    
    /**
     * Sends the translated message associated with the specified key with the specified arguments.
     * 
     * @param key the key
     * @param arguments the arguments
     */
    public void sendRawTranslation(String key, Object... arguments) {
        sender.sendMessage(translation.locale(locale).format(key, arguments));
    }
    
    
    /**
     * Returns a {@code Player} which represents this {@code CommandSource}, or {@code null}
     * if this {@code CommandSource} is not a {@code Player}.
     * 
     * @return a Player which represents this CommandSource if this CommandSource is a Player; else null
     */
    public @Nullable Player asPlayer() {
        return player;
    }
    
    /**
     * Returns whether this {@code CommandSource} is a {@code Player}.
     * 
     * @return true if this CommandSource is a Player; else false
     */
    public boolean isPlayer() {
        return player != null;
    }
    
    
    /**
     * Returns the underlying {@code CommandSender}.
     * 
     * @return the underlying CommandSender
     */
    public CommandSender get() {
        return sender;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public MessageTranslation getTranslation() {
        return translation;
    }
    
    /**
     * Sets the {@code MessageTranslation} for this {@code CommandSource}.
     * 
     * @param translation the translation
     */
    public void setTranslation(MessageTranslation translation) {
        this.translation = translation;
    }
    
    /**
     * Returns the {@code Locale} of this {@code CommandSource}.
     * 
     * @return the Locale
     */
    public Locale getLocale() {
        return locale;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOp() {
        return sender.isOp();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setOp(boolean value) {
        sender.setOp(value);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPermissionSet(String name) {
        return sender.isPermissionSet(name);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPermissionSet(Permission perm) {
        return sender.isPermissionSet(perm);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPermission(String name) {
        return sender.hasPermission(name);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPermission(Permission perm) {
        return sender.hasPermission(perm);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return sender.addAttachment(plugin, name, value);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return sender.addAttachment(plugin);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return sender.addAttachment(plugin, name, value, ticks);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return sender.addAttachment(plugin, ticks);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        sender.removeAttachment(attachment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void recalculatePermissions() {
        sender.recalculatePermissions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return sender.getEffectivePermissions();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(String message) {
        sender.sendMessage(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(String[] messages) {
        sender.sendMessage(messages);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Server getServer() {
        return sender.getServer();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return sender.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Spigot spigot() {
        return sender.spigot();
    }
    
}
