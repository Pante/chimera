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

import com.karuslabs.annotations.JDK9;
import com.karuslabs.commons.command.annotation.resolvers.*;
import com.karuslabs.commons.command.parser.*;
import com.karuslabs.commons.locale.providers.Provider;

import javax.annotation.Nullable;

import org.bukkit.plugin.Plugin;

import static com.karuslabs.commons.configuration.Configurations.from;


public class Commands {
    
    Plugin plugin;
    ProxiedCommandMap map;
    Provider provider;
    References references;
    CommandResolver resolver;
    
    
    public Commands(Plugin plugin, References references) {
        this(plugin, Provider.DETECTED, references);
    }
    
    public Commands(Plugin plugin, Provider provider, References references) {
        this(plugin, provider, references, CommandResolver.simple(plugin, references));
    }
    
    public Commands(Plugin plugin, Provider provider, References references, CommandResolver resolver) {
        this(plugin, new ProxiedCommandMap(plugin.getServer()), provider, references, resolver);
    }
    
    public Commands(Plugin plugin, ProxiedCommandMap map, Provider provider, References references, CommandResolver resolver) {
        this.plugin = plugin;
        this.map = map;
        this.provider = provider;
        this.references = references;
        this.resolver = resolver;
    }
    
    
    @JDK9
    public Commands load(String path) {
        try (References ref = references) {
            map.registerAll(plugin.getName(), loadParser().parse(from(getClass().getClassLoader().getResourceAsStream(path))));
        }
        
        return this;
    }
    
    protected Parser loadParser() {
        return Parsers.newParser(plugin, resolver, plugin.getDataFolder(), references, NullHandle.NONE, provider);
    }
    
    
    public Commands register(CommandExecutor executor) {
        resolver.resolve(map, executor);
        return this;
    }
    
    public Commands register(CommandExecutor executor, String... namespace) {
        resolver.resolve(map, executor, namespace);
        return this;
    }
    
    
    public @Nullable Command getCommand(String name) {
        return map.getCommand(name);
    }
    
    public ProxiedCommandMap getProxiedCommandMap() {
        return map;
    }
    
    public References getReferences() {
        return references;
    }
    
    public CommandResolver getResolver() {
        return resolver;
    }
    
}
