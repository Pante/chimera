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
import java.util.function.Function;
import javax.annotation.Nullable;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.*;
import org.bukkit.plugin.Plugin;

import static org.bukkit.ChatColor.translateAlternateColorCodes;


public class CommandSource implements CommandSender, Translatable {
    
    private CommandSender sender;
    private MessageTranslation translation;
    private @Nullable Player player;
    private Locale locale;
    
    
    public CommandSource(CommandSender sender, MessageTranslation translation) {
        this.sender = sender;
        this.translation = translation;
        this.player = (sender instanceof Player) ? (Player) sender : null;
        this.locale = Locales.getOrDefault(player != null ? player.getLocale() : "123");
    }
    
    
    public void sendColouredTranslation(String key, Object... arguments) {
        sender.sendMessage(translateAlternateColorCodes('&', translation.locale(locale).format(key, arguments)));
    }
    
    public void sendFormattedTranslation(String key, Function<String, String> format, Object... arguments) {
        sender.sendMessage(format.apply(translation.locale(locale).format(key, arguments)));
    }
    
    public void sendRawTranslation(String key, Object... arguments) {
        sender.sendMessage(translation.locale(locale).format(key, arguments));
    }
    
    
    public @Nullable Player asPlayer() {
        return player;
    }
    
    public boolean isPlayer() {
        return player != null;
    }
    
    
    public CommandSender get() {
        return sender;
    }
    
    @Override
    public MessageTranslation getTranslation() {
        return translation;
    }
    
    public void setTranslation(MessageTranslation translation) {
        this.translation = translation;
    }
    
    public Locale getLocale() {
        return locale;
    }

    
    @Override
    public boolean isOp() {
        return sender.isOp();
    }

    @Override
    public void setOp(boolean value) {
        sender.setOp(value);
    }

    @Override
    public boolean isPermissionSet(String name) {
        return sender.isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return sender.isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(String name) {
        return sender.hasPermission(name);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return sender.hasPermission(perm);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return sender.addAttachment(plugin, name, value);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return sender.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return sender.addAttachment(plugin, name, value, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return sender.addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        sender.removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        sender.recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return sender.getEffectivePermissions();
    }

    @Override
    public void sendMessage(String message) {
        sender.sendMessage(message);
    }

    @Override
    public void sendMessage(String[] messages) {
        sender.sendMessage(messages);
    }

    @Override
    public Server getServer() {
        return sender.getServer();
    }

    @Override
    public String getName() {
        return sender.getName();
    }

    @Override
    public Spigot spigot() {
        return sender.spigot();
    }
    
}
