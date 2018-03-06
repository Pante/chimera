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

import java.io.*;
import java.net.*;
import java.util.*;

import org.bukkit.plugin.java.JavaPlugin;

import org.reflections.Reflections;

import static java.util.stream.Collectors.toSet;


/**
 * Represents a resolver for subclasses of JavaPlugin annotated with {@code @Plugin}.
 */
public class Resolver {
    
    private List<String> elements;
    
    
    /**
     * Constructs a {@code Resolver} with the specified classpath elements.
     * 
     * @param elements the elements
     */
    public Resolver(List<String> elements) {
        this.elements = elements;
    }
    
    
    /**
     * Scans the classpath elements for subclasses of JavaPlugin annotated with {@code @Plugin}.
     * 
     * @return the subclass of JavaPlguin annotated with @Plugin
     * @throws ProcessorException if no or several of JavaPlugin annotated with @Plugin exists
     */
    public Class<? extends JavaPlugin> resolve() {
        Set<Class<? extends JavaPlugin>> plugins = load();
        if (plugins.size() == 1) {
            return plugins.toArray(new Class[] {})[0];
            
        } else if (plugins.isEmpty()) {
            throw new ProcessorException("Failed to find JavaPlugin subclass annotated with @Plugin");
            
        } else {
            throw new ProcessorException("Conflicting main classes, project must contain only 1 JavaPlugin subclass annotated with @Plugin");
        }
    }
    
    /**
     * Scans the classpath elements for subclasses of JavaPlugin annotated with {@code Plugin}.
     * 
     * @return the subclasses of JavaPlugin annotated with @Plugin
     */
    protected Set<Class<? extends JavaPlugin>> load() {
        URL[] urls = elements.stream().map(element -> {
            try {
                return new File(element).toURI().toURL();

            } catch (MalformedURLException e) {
                throw new UncheckedIOException(e);
            }
        }).toArray(URL[]::new);

        Reflections reflections = new Reflections(URLClassLoader.newInstance(urls, getClass().getClassLoader()));
        return reflections.getSubTypesOf(JavaPlugin.class).stream().filter(type -> type.getAnnotation(Plugin.class) != null).collect(toSet());
    }
    
}
