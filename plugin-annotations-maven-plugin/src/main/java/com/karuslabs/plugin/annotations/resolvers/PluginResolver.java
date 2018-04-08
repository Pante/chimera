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
package com.karuslabs.plugin.annotations.resolvers;

import com.karuslabs.commons.util.Null;
import com.karuslabs.plugin.annotations.annotations.Plugin;

import java.util.List;

import org.apache.maven.project.MavenProject;
import org.apache.maven.model.Developer;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;


public class PluginResolver implements Resolver {
    
    private MavenProject project;
    
    
    public PluginResolver(MavenProject project) {
        this.project = project;
    }
    
    
    @Override
    public void resolve(Class<? extends JavaPlugin> plugin, ConfigurationSection config) {
        Plugin annotation = plugin.getAnnotation(Plugin.class);
        name(annotation, config);
        main(plugin, config);
        version(annotation, config);
        
        description(annotation, config);
        config.set("author", Null.ifEmpty(annotation.author()));
        authors(annotation, config);
        config.set("prefix", Null.ifEmpty(annotation.prefix()));
        website(annotation, config);
        config.set("database", Null.ifEquals(annotation.database(), false));
        config.set("depend", Null.ifEmpty(annotation.depend()));
        config.set("softdepend", Null.ifEmpty(annotation.softdepend()));
        config.set("loadbefore", Null.ifEmpty(annotation.loadbefore()));
        config.set("load", annotation.load().getName());
    }
    
    protected void name(Plugin annotation, ConfigurationSection config) {
        if (annotation.name().equals("${project.name}")) {
            config.set("name", project.getName());
            
        } else {
            config.set("name", annotation.name());
        }
    }
    
    protected void main(Class<? extends JavaPlugin> plugin, ConfigurationSection config) {
        config.set("main", plugin.getName());
    }
    
    protected void version(Plugin annotation, ConfigurationSection config) {
        if (annotation.version().equals("${project.version}")) {
            config.set("version", project.getVersion());
            
        } else {
            config.set("version", annotation.version());
        }
    }
    
    protected void description(Plugin information, ConfigurationSection config) {
        if (information.description().equals("${project.description}")) {
            config.set("description", project.getDescription());
            
        } else {
            config.set("description", information.description());
        }
    }
    
    protected void authors(Plugin information, ConfigurationSection config) {
        if (information.authors().length == 1 && information.authors()[0].equals("${project.developers}")) {
            List<Developer> developers = project.getDevelopers();
            config.set("authors", developers.stream().map(Developer::getName).collect(toList()));
            
        } else if (information.authors().length > 0) {
            config.set("authors", asList(information.authors()));
        }
    }
    
    protected void website(Plugin information, ConfigurationSection config) {
        if (information.website().equals("${project.url}")) {
            config.set("website", project.getUrl());
            
        } else {
            config.set("website", information.website());
        }
    }

    
    @Override
    public boolean isResolvable(Class<? extends JavaPlugin> plugin) {
        return plugin.getAnnotation(Plugin.class) != null;
    }
    
}
