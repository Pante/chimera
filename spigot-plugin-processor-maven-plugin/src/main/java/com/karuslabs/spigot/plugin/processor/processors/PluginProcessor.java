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


public class PluginProcessor implements Processor {
    
    private Log log;
    private List<String> elements;
    private boolean override;
    
    
    public PluginProcessor(Log log, List<String> elements, boolean override) {
        this.log = log;
        this.elements = elements;
        this.override = override;
    }
    
    
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
