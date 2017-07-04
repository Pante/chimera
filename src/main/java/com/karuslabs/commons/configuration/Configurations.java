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

import com.karuslabs.commons.util.location.LazyLocation;

import java.io.*;

import org.bukkit.*;
import org.bukkit.configuration.*;
import org.bukkit.configuration.file.YamlConfiguration;


public class Configurations {
    
    public static YamlConfiguration from(InputStream stream) {
        try {
            return YamlConfiguration.loadConfiguration(new InputStreamReader(stream, "UTF-8"));
            
        } catch (UnsupportedEncodingException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    
    public static Location getLocation(ConfigurationSection config) {
        return new Location(
            Bukkit.getWorld(config.getString("world")),
            config.getDouble("x"),
            config.getDouble("y"),
            config.getDouble("z"),
            (float) config.getDouble("yaw"),
            (float) config.getDouble("pitch")
        );
    }
    
    public static Location getLazyLocation(ConfigurationSection config) {
        return new LazyLocation(config.getString("world"), getLocation(config));
    }
    
    
    public static ConfigurationSection getOrDefault(ConfigurationSection config, ConfigurationSection value) {
        if (config != null) {
            return config;
            
        } else {
            return value;
        }
    }
    
    public static ConfigurationSection getOrBlank(ConfigurationSection config) {
        if (config != null) {
            return config;
            
        } else {
            return new MemoryConfiguration();
        }
    }
    
}
