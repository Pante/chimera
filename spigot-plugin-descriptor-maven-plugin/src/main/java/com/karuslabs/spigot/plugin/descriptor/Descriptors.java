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
package com.karuslabs.spigot.plugin.descriptor;

import com.karuslabs.spigot.plugin.descriptor.descriptors.*;

import java.util.*;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import org.bukkit.configuration.ConfigurationSection;

import static com.karuslabs.spigot.plugin.descriptor.descriptors.Descriptor.*;


public class Descriptors {
    
    private Map<String, Descriptor> required;
    private Map<String, Descriptor> descriptors;
    
    
    public Descriptors(Map<String, Descriptor> required, Map<String, Descriptor> descriptors) {
        this.required = required;
        this.descriptors = descriptors;
    }
    
    
    public void execute(ConfigurationSection config) throws MojoExecutionException {
        required.forEach((key, descriptor) -> descriptor.execute(config, key));
        
        Set<String> keys = config.getKeys(false);
        if (descriptors.keySet().containsAll(keys)) {
            try {
                keys.forEach(key -> {
                    Descriptor descriptor = descriptors.get(key);
                    if (descriptor != null) {
                        descriptor.execute(config, key);
                    }
                });
                
            } catch (DescriptorException e) {
                throw new MojoExecutionException(e.getMessage(), e);
            }
            
        } else{
            keys.removeAll(descriptors.keySet());
            throw new MojoExecutionException("Invalid keys: " + keys.toString() + " in plugin.yml, key must be valid: " + descriptors.keySet());
        }
    }
    
    
    public static Descriptors simple(Log log, String name, List<String> elements, String version, boolean override) {
        Map<String, Descriptor> required = new HashMap<>();
        required.put("name", new NameDescriptor(name, override));
        required.put("version", new VersionDescriptor(log, version, override));
        required.put("main", new PluginDescriptor(log, elements, override));
        
        Map<String, Descriptor> descriptors = new HashMap<>();
        descriptors.put("name", NONE);
        descriptors.put("version", NONE);
        descriptors.put("main", NONE);
        descriptors.put("description", STRING);
        descriptors.put("load", new EnumDescriptor("STARTUP", "POSTWORLD"));
        descriptors.put("author", STRING);
        descriptors.put("authors", STRING_LIST);
        descriptors.put("website", STRING);
        descriptors.put("database", BOOLEAN);
        descriptors.put("depend", STRING_LIST);
        descriptors.put("prefix", STRING);
        descriptors.put("softdepend", STRING_LIST);
        descriptors.put("loadbefore", STRING_LIST);
        descriptors.put("commands", new ConfigurationDescriptor(new CommandDescriptor()));
        descriptors.put("permissions", new ConfigurationDescriptor(new PermissionDescriptor()));
        
        return new Descriptors(required, descriptors);
    }
    
}
