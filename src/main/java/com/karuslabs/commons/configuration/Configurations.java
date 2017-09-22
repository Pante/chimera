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

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.*;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;


public class Configurations {
        
    public static Map<String, Object> flatten(ConfigurationSection config) {
        return stream(config).collect(toMap(identity(), config::get));
    }
    
    public static ConcurrentMap<String, Object> concurrentFlatten(ConfigurationSection config) {
        return stream(config).collect(toConcurrentMap(identity(), config::get));
    }
    
    private static Stream<String> stream(ConfigurationSection config) {
        return config.getKeys(true).stream().filter(key -> !config.isConfigurationSection(key));
    }
    
    
    public static YamlConfiguration from(InputStream stream) {
        try {
            return YamlConfiguration.loadConfiguration(new InputStreamReader(stream, "UTF-8"));
            
        } catch (UnsupportedEncodingException e) {
            throw new UncheckedIOException(e);
        }
    }
    
}
