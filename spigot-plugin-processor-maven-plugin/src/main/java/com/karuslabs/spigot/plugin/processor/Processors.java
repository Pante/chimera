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
package com.karuslabs.spigot.plugin.processor;

import com.karuslabs.spigot.plugin.processor.processors.*;

import java.util.*;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import org.bukkit.configuration.ConfigurationSection;

import static com.karuslabs.spigot.plugin.processor.processors.Processor.*;


/**
 * Represents the processors for a plugin.yml
 */
public class Processors {
    
    Map<String, Processor> required;
    Map<String, Processor> processors;
    
    
    /**
     * Constructs a {@code Processors} with the specified required {@code Processor}s and other {@code Processor}s.
     * 
     * @param required the required processors
     * @param processors the processors
     */
    public Processors(Map<String, Processor> required, Map<String, Processor> processors) {
        this.required = required;
        this.processors = processors;
    }
    
    
    /**
     * Delegates processing to the required and other {@code Processor}s.
     * 
     * Required processors are ran regardless whether they are present in the plugin.yml.
     * 
     * @param config the COnfigurationSection
     * @throws MojoExecutionException if the ConfigurationSection contains invalid keys or values
     */
    public void execute(ConfigurationSection config) throws MojoExecutionException {
        required.forEach((key, descriptor) -> descriptor.execute(config, key));
        
        Set<String> keys = config.getKeys(false);
        if (processors.keySet().containsAll(keys)) {
            try {
                keys.forEach(key -> {
                    Processor descriptor = processors.get(key);
                    if (descriptor != null) {
                        descriptor.execute(config, key);
                    }
                });
                
            } catch (ProcessorException e) {
                throw new MojoExecutionException(e.getMessage(), e);
            }
            
        } else{
            keys.removeAll(processors.keySet());
            throw new MojoExecutionException("Invalid keys: " + keys.toString() + " in plugin.yml, key must be valid: " + processors.keySet());
        }
    }
    
    
    /**
     * Creates a {@code Processors} for processing a plugin.yml which follows the 
     * <a href = "https://bukkit.gamepedia.com/Plugin_YAML">Bukkit specifications</a>.
     * 
     * @param log the log
     * @param name the name
     * @param elements the classpath elements
     * @param version the verison
     * @param override the override
     * @return a Processors
     */
    public static Processors simple(Log log, String name, List<String> elements, String version, boolean override) {
        Map<String, Processor> required = new HashMap<>();
        required.put("name", new NameProcessor(name, override));
        required.put("version", new VersionProcessor(log, version, override));
        required.put("main", new PluginProcessor(log, elements, override));
        
        Map<String, Processor> descriptors = new HashMap<>();
        descriptors.put("name", NONE);
        descriptors.put("version", NONE);
        descriptors.put("main", NONE);
        descriptors.put("description", STRING);
        descriptors.put("load", new EnumProcessor("STARTUP", "POSTWORLD"));
        descriptors.put("author", STRING);
        descriptors.put("authors", STRING_LIST);
        descriptors.put("website", STRING);
        descriptors.put("database", BOOLEAN);
        descriptors.put("depend", STRING_LIST);
        descriptors.put("prefix", STRING);
        descriptors.put("softdepend", STRING_LIST);
        descriptors.put("loadbefore", STRING_LIST);
        descriptors.put("commands", new DelegateProcessor(new CommandProcessor()));
        descriptors.put("permissions", new DelegateProcessor(new PermissionProcessor()));
        
        return new Processors(required, descriptors);
    }
    
}
