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

import com.karuslabs.mockkit.rule.ServerRule;

import org.bukkit.*;
import org.bukkit.configuration.*;
import org.bukkit.configuration.file.YamlConfiguration;

import org.junit.*;

import static com.karuslabs.commons.Yaml.CONFIGURATION;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class ConfigurationsTest {
    
    @Rule
    public ServerRule server = new ServerRule();
    
    private World world;
    
    
    public ConfigurationsTest() {
        world = mock(World.class);
        when(server.getServer().getWorld("name")).thenReturn(world).getMock();
    }
    
    
    @Test
    public void getLocation() {
        Location location = Configurations.getLocation(CONFIGURATION.getConfigurationSection("location"));
        assertEquals(world, location.getWorld());
        assertEquals(0, location.getX(), 0);
        assertEquals(1, location.getY(), 0);
        assertEquals(2, location.getZ(), 0);
        assertEquals(0.3, location.getYaw(), 0.0001);
        assertEquals(0.4, location.getPitch(), 0.0001);
    }
    
    
    @Test
    public void getLazyLocation() {
        Location location = Configurations.getLazyLocation(CONFIGURATION.getConfigurationSection("location"));
        assertEquals(world, location.getWorld());
        assertEquals(0, location.getX(), 0);
        assertEquals(1, location.getY(), 0);
        assertEquals(2, location.getZ(), 0);
        assertEquals(0.3, location.getYaw(), 0.0001);
        assertEquals(0.4, location.getPitch(), 0.0001);
    }
    
    
    
    @Test
    public void from() {
        YamlConfiguration config = Configurations.from(getClass().getClassLoader().getResourceAsStream("configuration/config.yml"));
        assertNotNull(config);
    }
    
    
    @Test
    public void getOrDefault() {
        ConfigurationSection config = mock(ConfigurationSection.class);
        assertTrue(config == Configurations.getOrDefault(config, new MemoryConfiguration()));
    }
    
    
    @Test
    public void getOrDefault_ReturnsDefault() {
        ConfigurationSection config = mock(ConfigurationSection.class);
        assertTrue(config == Configurations.getOrDefault(null, config));
    }
    
    
    @Test
    public void getOrBlank() {
        ConfigurationSection config = mock(ConfigurationSection.class);
        assertTrue(config == Configurations.getOrBlank(config));
    }
    
}
