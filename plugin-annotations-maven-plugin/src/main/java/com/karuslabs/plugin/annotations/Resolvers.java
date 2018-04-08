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
package com.karuslabs.plugin.annotations;

import com.karuslabs.plugin.annotations.resolvers.*;

import java.util.*;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;


public class Resolvers {
    
    private Provider provider;
    private Set<Resolver> resolvers;
    
    
    public Resolvers(Provider provider, Set<Resolver> resolvers) {
        this.provider = provider;
        this.resolvers = resolvers;
    }
    
    
    public void resolve(Log log, ConfigurationSection config) {      
        Class<? extends JavaPlugin> plugin = provider.provide();
        
        log.info("Main class found, processing annotations");
        for (Resolver resolver : resolvers) {
            if (resolver.isResolvable(plugin)) {
                resolver.resolve(plugin, config);
            }
        }
    }
    
    
    public static Resolvers simple(MavenProject project, List<String> elements) {
        Set<Resolver> resolvers = new HashSet<>(3);
        resolvers.add(new PluginResolver(project));
        resolvers.add(new CommandResolver());
        resolvers.add(new PermissionResolver());
        
        return new Resolvers(new Provider(elements), resolvers);
    }
    
}
