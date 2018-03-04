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

import com.karuslabs.plugin.annotations.annotations.Plugin;
import com.karuslabs.plugin.annotations.plugin.TestPlugin;

import java.util.stream.Stream;

import org.apache.maven.project.MavenProject;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class PluginProcessorTest {
    
    MavenProject project = mock(MavenProject.class);
    PluginProcessor processor = spy(new PluginProcessor(project));
    ConfigurationSection config = mock(ConfigurationSection.class);
    
    
    @Plugin
    static class Default extends JavaPlugin {
        
    }
    
    
    @Plugin(name = "specified name", version = "specified version")
    static class Specified extends JavaPlugin {
        
    }
    
    
    @Test
    void process() {
        processor.process(Specified.class, config);
        Plugin plugin = Specified.class.getAnnotation(Plugin.class);
        
        verify(processor).processName(plugin, config);
        verify(processor).processMain(Specified.class, config);
        verify(processor).processVersion(plugin, config);
    }
    
    
    @ParameterizedTest
    @CsvSource({"default, default name", "specified, specified name"})
    void processName(String type, String expected) {
        when(project.getName()).thenReturn("default name");
        
        processor.processName(from(type), config);
        
        verify(config).set("name", expected);
    }
    
    
    @Test
    void processMain() {
        processor.processMain(TestPlugin.class, config);
        
        verify(config).set("main", TestPlugin.class.getName());
    }
    
    
    @ParameterizedTest
    @CsvSource({"default, default version", "specified, specified version"})
    void processVersion(String type, String expected) {
        when(project.getVersion()).thenReturn("default version");
        
        processor.processVersion(from(type), config);
        
        verify(config).set("version", expected);
    }
    
    
    static Plugin from(String name) {
        if (name.equals("default")) {
            return Default.class.getAnnotation(Plugin.class);
            
        } else {
            return Specified.class.getAnnotation(Plugin.class);
        }
    }
    
    
    @ParameterizedTest
    @MethodSource("isAnnotated_parameters")
    void isAnnotated(Class<? extends JavaPlugin> type, boolean expected) {
        assertEquals(expected, processor.isAnnotated(type));
    }
    
    static Stream<Arguments> isAnnotated_parameters() {
        return Stream.of(of(Default.class, true), of(Specified.class, true), of(JavaPlugin.class, false));
    }
    
}
