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

import com.karuslabs.plugin.annotations.processors.*;

import java.util.*;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * Represents the processors for generating a plugin.yml
 */
public class Processors {

    private Resolver resolver;
    private Set<Processor> processors;
    
    
    /**
     * Constructs a {@code Processors} with the specified {@code Resolver} and {@code Processor}s.
     * 
     * @param resolver the resolver
     * @param processors the processors
     */
    public Processors(Resolver resolver, Set<Processor> processors) {
        this.resolver = resolver;
        this.processors = processors;
    }
    
    
    /**
     * Resolves and processes the plugin annotations using the {@code Resolver}
     * and {@code Processor}s.
     * 
     * @param log the logger
     * @param config the ConfigurationSection
     */
    public void process(Log log, ConfigurationSection config) {      
        Class<? extends JavaPlugin> plugin = resolver.resolve();
        
        log.info("Main class found, processing annotations");
        for (Processor processor : processors) {
            if (processor.isAnnotated(plugin)) {
                processor.process(plugin, config);
            }
        }
    }
    
    
    /**
     * Creates a {@code Processors} with the default settings.
     * 
     * @param project the project
     * @param elements the elements
     * @return the Processors
     */
    public static Processors simple(MavenProject project, List<String> elements) {
        Set<Processor> processors = new HashSet<>();
        processors.add(new PluginProcessor(project));
        processors.add(new InformationProcessor(project));
        processors.add(new CommandProcessor());
        processors.add(new PermissionProcessor());
        
        return new Processors(new Resolver(elements), processors);
    }
    
}
