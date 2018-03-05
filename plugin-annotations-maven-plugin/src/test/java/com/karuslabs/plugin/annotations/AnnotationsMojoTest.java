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
package com.karuslabs.plugin.annotations;

import com.karuslabs.plugin.annotations.plugin.TestPlugin;

import java.io.*;

import org.apache.maven.plugin.*;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import org.bukkit.configuration.file.YamlConfiguration;

import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class AnnotationsMojoTest {
    
    AnnotationsMojo mojo = spy(new AnnotationsMojo());
    
    
    @Test
    void execute() throws MojoExecutionException, MojoFailureException, IOException {   
        mojo.project = mock(MavenProject.class);
        mojo.file = mock(File.class);
        mojo.elements = asList(TestPlugin.class.getResource("").getPath());
        
        YamlConfiguration config = spy(YamlConfiguration.loadConfiguration(new File(getClass().getClassLoader().getResource("annotations.yml").getPath())));
        doNothing().when(config).save(any(File.class));
        doReturn(config).when(mojo).loadConfiguration();
        doReturn(mock(Log.class)).when(mojo).getLog();
        
        mojo.execute();
        
        verify(config).set("version", "test version");
        verify(config).save(mojo.file);
    }
    
    
    @Test
    void execute_ThrowsException() throws MojoExecutionException, MojoFailureException, IOException {
        mojo.project = mock(MavenProject.class);
        mojo.file = mock(File.class);
        mojo.elements = asList(TestPlugin.class.getResource("").getPath());
        
        YamlConfiguration config = spy(YamlConfiguration.loadConfiguration(new File(getClass().getClassLoader().getResource("annotations.yml").getPath())));
        doThrow(IOException.class).when(config).save(mojo.file);
        doReturn(config).when(mojo).loadConfiguration();
        doReturn(mock(Log.class)).when(mojo).getLog();
        
        assertThrows(MojoExecutionException.class, mojo::execute);
    }
    
    
    @Test
    void loadConfiguration() throws MojoExecutionException {
        mojo.file = new File(getClass().getClassLoader().getResource("annotations.yml").getPath());
        mojo.elements = asList("com.karuslabs.plugin.annotations");
        assertEquals("annotations", mojo.loadConfiguration().getString("plugin"));
    }
    
    
    @Test
    void loadConfiguation_ThrowsException() {
        assertEquals(
            IllegalArgumentException.class,
            assertThrows(MojoExecutionException.class, () -> mojo.loadConfiguration()).getCause().getClass()
        );
    }
    
}
