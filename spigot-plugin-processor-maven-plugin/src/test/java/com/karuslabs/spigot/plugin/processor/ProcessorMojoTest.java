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
package com.karuslabs.spigot.plugin.processor;

import java.io.*;
import static java.util.Collections.EMPTY_LIST;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.*;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import org.bukkit.configuration.file.YamlConfiguration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ProcessorMojoTest {
    
    ProcessorMojo mojo = spy(new ProcessorMojo());
    Log log = mock(Log.class);
    File file = mock(File.class);
    
    
    @Test
    void execute() throws MojoExecutionException, MojoFailureException, IOException {
        doReturn(log).when(mojo).getLog();
        mojo.file = file;
        
        YamlConfiguration config = mock(YamlConfiguration.class);
        doReturn(config).when(mojo).loadConfiguration();
        
        Processors processors = mock(Processors.class);
        doReturn(processors).when(mojo).processors();
        
        mojo.execute();
        
        verify(processors).execute(config);
        verify(config).save(file);
    }
    
    
    @Test
    void execute_ThrowsException() throws MojoExecutionException, MojoFailureException, IOException {
        doReturn(log).when(mojo).getLog();
        mojo.file = file;
        
        YamlConfiguration config = mock(YamlConfiguration.class);
        doThrow(IOException.class).when(config).save(file);
        doReturn(config).when(mojo).loadConfiguration();
        
        Processors processors = mock(Processors.class);
        doReturn(processors).when(mojo).processors();
        
        assertEquals(
            IOException.class,
            assertThrows(MojoExecutionException.class, mojo::execute).getCause().getClass()
        );
    }
    
    
    @Test
    void loadConfiguration() throws MojoExecutionException {
        mojo.file = new File(getClass().getClassLoader().getResource("plugin.yml").getPath());
        assertEquals(1, mojo.loadConfiguration().getInt("invalid-value.description"));
    }
    
    
    @Test
    void loadConfiguration_ThrowsException() {
        assertEquals(
            IllegalArgumentException.class,
            assertThrows(MojoExecutionException.class, () -> mojo.loadConfiguration()).getCause().getClass()
        );
    }
    
    
    @Test
    void processors() throws MojoFailureException, DependencyResolutionRequiredException {
        mojo.project = when(mock(MavenProject.class).getCompileClasspathElements()).thenReturn(EMPTY_LIST).getMock();
        assertNotNull(mojo.processors());
    }
    
    
    @Test
    void processors_ThrowsException() throws DependencyResolutionRequiredException {
        mojo.project = when(mock(MavenProject.class).getCompileClasspathElements()).thenThrow(DependencyResolutionRequiredException.class).getMock();
        
        assertEquals(
            DependencyResolutionRequiredException.class,
            assertThrows(MojoFailureException.class, () -> mojo.processors()).getCause().getClass()
        );
    }
    
}
