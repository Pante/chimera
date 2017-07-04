/* 
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
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
package com.karuslabs.commons.configuration;

import com.google.common.io.Files;
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
    
    
    public YamlFileConfiguration loadOrDefault(String defaultPath) {
        try {
            if (!file.exists()) {
                load(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(defaultPath)));
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
