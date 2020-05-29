/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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
package com.karuslabs.commons.command.dispatcher;

import com.mojang.brigadier.tree.LiteralCommandNode;

import org.bukkit.command.CommandSender;

import org.checkerframework.checker.nullness.qual.Nullable;


/**
 * A {@code DispatcherMap} wraps and registers {@code LiteralCommandNode}s to Spigot.
 */
public interface DispatcherMap {
    
    /**
     * Registers the given command to Spigot.
     * 
     * @param command the command
     * @return a {@code DispatcherCommand} that represents the given command, or
     *         {@code null} if the command could not be registered
     */
    @Nullable DispatcherCommand register(LiteralCommandNode<CommandSender> command);

    /**
     * Removes the given command from Spigot.
     * 
     * @param name the name of the command
     * @return a {@code DispatcherCommand} that represents the removed command,
     *         or {@code null} if the command was not registered
     */
    @Nullable DispatcherCommand unregister(String name);

}
