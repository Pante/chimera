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
import com.karuslabs.plugin.annotations.annotations.Command;

import java.util.*;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * Represents a processor for {@code Command} annotations.
 */
public class CommandProcessor implements Processor {

    /**
     * Processes the {@code @Command} annotation for the specified subclass of JavaPlugin and saves the results to the {@code ConfigurationSection}.
     * 
     * @param plugin the class
     * @param config the ConfigurationSection
     * @throws ProcessorException if there are conflicting commands
     */
    @Override
    public void process(Class<? extends JavaPlugin> plugin, ConfigurationSection config) {
        Set<String> names = new HashSet<>();
        Command[] commands = plugin.getAnnotationsByType(Command.class);
        if (commands.length != 0) {
            config = config.createSection("commands");
            for (Command command : commands) {
                if (names.add(command.name())) {
                    process(command, config);

                } else {
                    throw new ProcessorException("Conflicting command names: " + command.name() + ", command names must be unique");
                }
            }
        }
    }
    
     /**
     * Processes the information for the specified {@code @Command} annotation and outputs the results to the {@code ConfigurationSection}.
     * 
     * @param command the Command annotation
     * @param config the ConfigurationSection
     */
    protected void process(Command command, ConfigurationSection config) {
        config = config.createSection(command.name());
        config.set("aliases", Null.ifEmpty(command.aliases()));
        config.set("description", Null.ifEmpty(command.description()));
        config.set("permission", Null.ifEmpty(command.permission()));
        config.set("permission-message", Null.ifEmpty(command.message()));
        config.set("usage", Null.ifEmpty(command.usage()));
    }

    /**
     * Checks if the specified class is annotated with {@code @Comand}.
     * 
     * @param plugin the plugin
     * @return true if the specified class is annotated with @Command; else false
     */
    @Override
    public boolean isAnnotated(Class<? extends JavaPlugin> plugin) {
        return plugin.getAnnotationsByType(Command.class).length != 0;
    }
    
}
