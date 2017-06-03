/*
 * Copyright (C) 2017 Karus Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.karuslabs.commons.configuration;

import java.io.*;

import org.bukkit.configuration.*;
import org.bukkit.configuration.file.YamlConfiguration;


public class Configurations {
        
    public static final ImmutableConfigurationSection BLANK = new ImmutableConfigurationSection(new MemoryConfiguration());

    
    public static YamlConfiguration from(InputStream stream) {
        return YamlConfiguration.loadConfiguration(new InputStreamReader(stream));
    }
    
    
    public static ConfigurationSection getOrDefault(ConfigurationSection config, ConfigurationSection defaultConfig) {
        if (config != null) {
            return config;
            
        } else {
            return defaultConfig;
        }
    }
    
    public static ConfigurationSection getOrBlank(ConfigurationSection config) {
        if (config != null) {
            return config;
            
        } else {
            return BLANK;
        }
    }
    
}
