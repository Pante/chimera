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
package com.karuslabs.plugin.annotations.resolvers;

import com.karuslabs.commons.configuration.Configurations;

import java.util.List;
import java.util.stream.Stream;

import org.apache.maven.model.Developer;
import org.apache.maven.project.MavenProject;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;
import com.karuslabs.plugin.annotations.annotations.Plugin;
import com.karuslabs.plugin.annotations.plugin.TestPlugin;
import org.junit.jupiter.api.Test;


class PluginResolverTest {
    
    List<Developer> developers = asList(new Developer());
    MavenProject project = mock(MavenProject.class);
    PluginResolver resolver = spy(new PluginResolver(project));
    ConfigurationSection config = Configurations.from(getClass().getClassLoader().getResourceAsStream("annotations.yml"));
    ConfigurationSection mock = mock(ConfigurationSection.class);
    
    
    @Plugin
    static class Default extends JavaPlugin {
        
    }
    
    @Plugin(
        name = "specified name", version = "specified version", 
        description = "specified description", authors = {"Pante"}, website = "https://repo.karuslabs.com"
    )
    static class Specified extends JavaPlugin {
        
    }
    
    
    @ParameterizedTest
    @MethodSource("resolve_parameters")
    void resolve(Class<? extends JavaPlugin> type, String description, List<String> names, String website) {
        developers.get(0).setName("random dude");
        when(project.getDescription()).thenReturn("default description");
        when(project.getDevelopers()).thenReturn(developers);
        when(project.getUrl()).thenReturn("default website");
        
        resolver.resolve(type, config);
        
        verify(resolver).name(type.getAnnotation(Plugin.class), config);
        verify(resolver).main(type, config);
        verify(resolver).version(type.getAnnotation(Plugin.class), config);
        
        assertEquals(description, config.getString("description"));
        assertEquals(names, config.getStringList("authors"));
        assertEquals(website, config.getString("website"));
    }
    
    static Stream<Arguments> resolve_parameters() {
        return Stream.of(
            of(Default.class, "default description", asList("random dude"), "default website"),
            of(Specified.class, "specified description", asList("Pante"), "https://repo.karuslabs.com")
        );
    }
    
    
    @ParameterizedTest
    @CsvSource({"default, default name", "specified, specified name"})
    void name(String type, String expected) {
        when(project.getName()).thenReturn("default name");
        
        resolver.name(from(type), mock);
        
        verify(mock).set("name", expected);
    }
    
    
    @Test
    void main() {
        resolver.main(TestPlugin.class, mock);
        
        verify(mock).set("main", TestPlugin.class.getName());
    }
    
    
    @ParameterizedTest
    @CsvSource({"default, default version", "specified, specified version"})
    void version(String type, String expected) {
        when(project.getVersion()).thenReturn("default version");
        
        resolver.version(from(type), mock);
        
        verify(mock).set("version", expected);
    }
    
    
    static Plugin from(String name) {
        if (name.equals("default")) {
            return Default.class.getAnnotation(Plugin.class);
            
        } else {
            return Specified.class.getAnnotation(Plugin.class);
        }
    }
    
    
    @ParameterizedTest
    @MethodSource("isResolvable_parameters")
    void isResolvable(Class<? extends JavaPlugin> type, boolean expected) {
        assertEquals(expected, resolver.isResolvable(type));
    }
    
    static Stream<Arguments> isResolvable_parameters() {
        return Stream.of(of(Default.class, true), of(Specified.class, true), of(JavaPlugin.class, false));
    }
    
}

