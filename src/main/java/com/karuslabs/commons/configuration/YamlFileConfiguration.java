/*
 * Copyright (C) 2017 Karus Labs
 * All rights reserved.
 */
package com.karuslabs.commons.configuration;

import com.karuslabs.commons.util.UncheckedIOException;

import java.io.*;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;


public class YamlFileConfiguration extends YamlConfiguration {
    
    private File file;
    
    
    public YamlFileConfiguration(File file) {
        this.file = file;
    }
    
    
    public YamlFileConfiguration load() {
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            load(file);
            return this;
            
        } catch (IOException | InvalidConfigurationException e) {
            throw new UncheckedIOException("Failed to load configuration file: " + file.getName(), e);
        }
    }
    
    
    public YamlFileConfiguration loadOrDefault(String path) {
        try {
            if (!file.exists()) {
                load(getClass().getClassLoader().getResourceAsStream(path));
                save(file);
                        
            } else {
                load(file);
            }
            
            return this;
            
        } catch (IOException | InvalidConfigurationException e) {
            throw new UncheckedIOException("Failed to load configuration file: " + file.getName(), e);
        }
    }
    
    
    public void save() {
        try {
            save(file);
            
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to save config: " + file.getName(), e);
        }
    }
    
}
