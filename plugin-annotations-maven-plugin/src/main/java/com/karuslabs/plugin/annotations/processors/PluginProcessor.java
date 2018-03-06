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
package com.karuslabs.plugin.annotations.processors;

import com.karuslabs.plugin.annotations.annotations.Plugin;

import org.apache.maven.project.MavenProject;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * Represents a processor for {@code @Plugin} annotations.
 */
public class PluginProcessor implements Processor {
    
    private MavenProject project;
    
    
    /**
     * Constructs a {@code PluginProcessor} with the specified project.
     * 
     * @param project the project
     */
    public PluginProcessor(MavenProject project) {
        this.project = project;
    }

    
    /**
     * Processes the {@code @Plugin} annotation for the specified subclass of JavaPlugin and saves the results to the {@code ConfigurationSection}.
     * 
     * @param plugin the class
     * @param config the Configuration
     */
    @Override
    public void process(Class<? extends JavaPlugin> plugin, ConfigurationSection config) {
        Plugin annotation = plugin.getAnnotation(Plugin.class);
        processName(annotation, config);
        processMain(plugin, config);
        processVersion(annotation, config);
    }
    
    /**
     * Processes the name of the specified {@code @Plugin} annotation and outputs the results to the {@code ConfigurationSection}.
     * 
     * @param annotation the Plugin annotation
     * @param config the ConfigurationSection
     */
    protected void processName(Plugin annotation, ConfigurationSection config) {
        if (annotation.name().equals("${project.name}")) {
            config.set("name", project.getName());
            
        } else {
            config.set("name", annotation.name());
        }
    }
    
    /**
     * Processes the subclass of JavaPlugin and outputs the results to the {@code ConfigurationSection}.
     * 
     * @param plugin the subclass of JavaPlugin
     * @param config the ConfigurationSection
     */
    protected void processMain(Class<? extends JavaPlugin> plugin, ConfigurationSection config) {
        config.set("main", plugin.getName());
    }
    
    /**
     * Processes the version of the specified {@code @Plugin} annotation and outputs the results to the {@code ConfigurationSection}.
     * 
     * @param annotation the Plugin annotation
     * @param config the ConfigurationSection
     */
    protected void processVersion(Plugin annotation, ConfigurationSection config) {
        if (annotation.version().equals("${project.version}")) {
            config.set("version", project.getVersion());
            
        } else {
            config.set("version", annotation.version());
        }
    }

    
    /**
     * Checks if the specified class is annotated with {@code @Plugin}.
     * 
     * @param plugin the plugin
     * @return true if the specified class is annotated with @Plugin
     */
    @Override
    public boolean isAnnotated(Class<? extends JavaPlugin> plugin) {
        return plugin.getAnnotation(Plugin.class) != null;
    }
    
}
