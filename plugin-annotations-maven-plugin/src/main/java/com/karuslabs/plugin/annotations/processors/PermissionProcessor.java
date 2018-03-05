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


public class PermissionProcessor implements Processor {
    
    Set<String> names;
    
    
    public PermissionProcessor() {
        names = new HashSet<>();
    }

    
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
    
    
    @Override
    public boolean isAnnotated(Class<? extends JavaPlugin> plugin) {
        return plugin.getAnnotationsByType(Permission.class).length != 0;
    }
    
}
