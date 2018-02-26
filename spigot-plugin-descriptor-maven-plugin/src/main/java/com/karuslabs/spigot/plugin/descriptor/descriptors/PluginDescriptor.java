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
package com.karuslabs.spigot.plugin.descriptor.descriptors;

import com.karuslabs.spigot.plugin.descriptor.UncheckedMojoFailureException;

import java.io.File;
import java.net.*;
import java.util.*;
import javax.annotation.Nullable;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.*;
import org.apache.maven.project.MavenProject;

import org.bukkit.configuration.ConfigurationSection;

import org.reflections.Reflections;


public class PluginDescriptor {
    
    private MavenProject project;
    private @Nullable String main;
    
    
    public PluginDescriptor(MavenProject project, String main) {
        this.project = project;
        this.main = main;
    }
    
    
    public void execute(ConfigurationSection config) throws MojoExecutionException, DependencyResolutionRequiredException {
        Reflections reflections = main == null ? detect() : resolve(main);
    }
    
    protected Reflections detect() throws DependencyResolutionRequiredException {
        List<String> elements = project.getCompileClasspathElements();
        URL[] urls = elements.stream().map(element -> {
            try {
                return new File(element).toURI().toURL();

            } catch (MalformedURLException e) {
                throw new UncheckedMojoFailureException("Unable to resolve classpath element to URL", e);
            }
        }).toArray(URL[]::new);

        return new Reflections(URLClassLoader.newInstance(urls, Thread.currentThread().getContextClassLoader()));
    }
    
    protected Reflections resolve(String main) {
        return new Reflections();
    }
    
}
