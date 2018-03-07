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

import com.karuslabs.commons.util.Null;
import com.karuslabs.plugin.annotations.annotations.*;

import java.util.*;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * Represents a processor for {@code Permission} annotations.
 */
public class PermissionProcessor implements Processor {
    
    Set<String> names;
    
    
    /**
     * Constructs a {@code PermissionProcessor}.
     */
    public PermissionProcessor() {
        names = new HashSet<>();
    }

    
    /**
     * Processes the {@code Permission} annotation for the specified class and outputs the results to the {@code ConfigurationSection}.
     * 
     * @param plugin the class
     * @param config the ConfigurationSection
     * @throws ProcessorException if there are conflicting permissions or unresolvable permission references
     */
    @Override
    public void process(Class<? extends JavaPlugin> plugin, ConfigurationSection config) {
        Permission[] permissions = plugin.getAnnotationsByType(Permission.class);
        if (permissions.length != 0) {
            config = config.createSection("permissions");
            for (Permission permission : permissions) {
                if (!names.add(permission.name())) {
                    throw new ProcessorException("Conflicting permissions: " + permission.name() + ", permissions must be unique");
                }
            }
            
            for (Permission permission : permissions) {
                process(permission, config);
            }
        }
    }
    
    /**
     * Processes the information for the specified {@code Permission} annotation and outputs the results to the {@code ConfigurationSection}.
     * 
     * @param permission the Plugin annotation
     * @param config the ConfigurationSection
     */
    protected void process(Permission permission, ConfigurationSection config) {
        if (!permission.description().isEmpty() || permission.children().length != 0) {
            config = config.createSection(permission.name());
            config.set("description", Null.ifEmpty(permission.description()));
            config.set("default", permission.value().getName());
            process(permission.children(), config);
            
        } else {
            config.set(permission.name(), permission.value().getName());
        }
    }
    
    /**
     * Processes the nested {@code @Child} annotations of the specified {@code Permission} annotation and outputs the results to the {@code ConfigurationSection}.
     * 
     * @param annotations the child annotations
     * @param config the ConfigurationSection
     * @throws ProcessorException if the child permission references an unresolvable permission or if there are conflicting child permissions
     */
    protected void process(Child[] annotations, ConfigurationSection config) {
        Set<String> children = new HashSet<>();
        if (annotations.length != 0) {
            config = config.createSection("children");
            for (Child child : annotations) {
                if (!names.contains(child.name())) {
                    throw new ProcessorException("Invalid child permission: " + child.name() + ", child permission must refer to a valid permission");
                }
                
                if (children.add(child.name())) {
                    config.set(child.name(), child.value());
                    
                } else {
                    throw new ProcessorException("Conflicting child permissions: " + child.name() + ", children must be unique");
                }
            }
        }
    }
    
    
    /**
     * Checks if the specified class has any {@code Permission} annotations.
     * 
     * @param plugin the class
     * @return true if the specified class has Permission annotations; else false
     */
    @Override
    public boolean isAnnotated(Class<? extends JavaPlugin> plugin) {
        return plugin.getAnnotationsByType(Permission.class).length != 0;
    }
    
}
