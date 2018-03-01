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
package com.karuslabs.spigot.plugin.processor.processors;

import com.karuslabs.spigot.plugin.processor.ProcessorException;

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.maven.plugin.logging.Log;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import org.reflections.Reflections;

import static java.util.stream.Collectors.toSet;


/**
 * A {@code Processor} implementation which processes value for main in a plugin.yml.
 */
public class PluginProcessor implements Processor {
    
    private Log log;
    private List<String> elements;
    private boolean override;
    
    
    /**
     * Constructs a {@code PluginProcessor} with the specified log, elements and override.
     * 
     * @param log the log
     * @param elements the elements
     * @param override the override
     */
    public PluginProcessor(Log log, List<String> elements, boolean override) {
        this.log = log;
        this.elements = elements;
        this.override = override;
    }
    
    
    /**
     * Sets the main class if no main class was specified is null or overriding is allowed.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     * @throws ProcessorException if the main class was specified but could not be found and overriding is not allowed
     */
    @Override
    public void execute(ConfigurationSection config, String key) { 
        String main = config.getString(key);
        Set<String> detected = load();
        
        if (main != null && detected.contains(main)) {
            return;
        }
        
        if (main == null || override) {
            setMain(config, detected);
            
        } else {
            throw new ProcessorException("Invalid main class specified: " + main + ", main must be a subclass of JavaPlugin");
        }
    }
    
    /**
     * Returns the paths to subclasses of JavaPlugin on the classpath.
     * 
     * @return the paths
     */
    protected Set<String> load() {
        URL[] urls = elements.stream().map(element -> {
            try {
                return new File(element).toURI().toURL();
                
            } catch (MalformedURLException e) {
                throw new UncheckedIOException(e);
            }
        }).toArray(URL[]::new);
        
        Reflections reflections = new Reflections(URLClassLoader.newInstance(urls, getClass().getClassLoader()));
        return reflections.getSubTypesOf(JavaPlugin.class).stream().map(Class::getName).collect(toSet());
    }
        
    /**
     * Sets the main class if only one JavaPlugin subclass was detected else
     * throws a {@code ProcessorException}.
     * 
     * @param config the config
     * @param detected the detected subclasses of JavaPlugin
     * @throws ProcessorException if the detected classes is not equal to 1
     */
    protected void setMain(ConfigurationSection config, Set<String> detected) {
        if (detected.size() == 1) {
            String main = detected.toArray(new String[0])[0];
            log.info("Detected and setting main to: " + main);
            
            config.set("main", main);
                
        } else if (detected.size() > 1) {
            throw new ProcessorException("Multiple JavaPlugin subclasses detected, main class must be specified manually");
            
        } else {
            throw new ProcessorException("No JavaPlugin subclass detected, main class must be specified manually");
        }   
    }
    
}
