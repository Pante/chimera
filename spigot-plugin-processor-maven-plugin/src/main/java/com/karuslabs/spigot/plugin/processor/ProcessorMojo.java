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
import javax.annotation.Nullable;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.*;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import org.bukkit.configuration.file.YamlConfiguration;

import static org.apache.maven.plugins.annotations.LifecyclePhase.COMPILE;


/**
 * 
 */
@Mojo(name = "descriptor", defaultPhase = COMPILE, threadSafe = false)
public class ProcessorMojo extends AbstractMojo {
    
    /**
     * The project model.
     */
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    protected MavenProject project;
    
    /**
     * The location of the plugin.yml which defaults to ${project.basedir}/src/main/resources/plugin.yml
     */
    @Parameter(defaultValue = "${project.basedir}/src/main/resources/plugin.yml")
    protected File file;
    
    /**
     * The plugin name which defaults to the project name specified in the pom.xml.
     */
    @Parameter(defaultValue = "${project.name}", required = true)
    protected String name;
        
    /**
     * The plugin version which defaults to the project version specified in the pom.xml.
     */
    @Parameter(defaultValue = "${project.version}", required = true)
    protected String version;
    
    /**
     * The plugin main class.
     * 
     * Not specifying the main class will allow this {@code ProcessorMojo} to automatically detect the main class.
     */
    @Parameter
    protected @Nullable String main;
    
    /**
     * The override which determines if the overriding values in the plugin.yml is allowed, defaulting to {@code true}.
     */
    @Parameter(defaultValue = "true", required = true)
    protected boolean override;
    
    
    /**
     * Executes this {@code ProcessorMojo}.
     * 
     * @throws MojoExecutionException if this ValidatorMojo fails to process the YAML file
     * @throws MojoFailureException if an internal exception occurs
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Loading plugin.yml: " + file.getPath());
        YamlConfiguration config = loadConfiguration();
        
        getLog().info("Loading descriptors");
        Processors processors = processors();
        
        getLog().info("Validating plugin.yml");
        processors.execute(config);
        getLog().info("Found no errors in plugin.yml");
        
        try {
            config.save(file);
            
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
    
    /**
     * Loads the plugin.yml specified in the pom.xml
     * 
     * @return the plugin.yml
     * @throws MojoExecutionException if theplugin.yml could not be loaded
     */
    protected YamlConfiguration loadConfiguration() throws MojoExecutionException {
        try {
            return YamlConfiguration.loadConfiguration(file);
            
        } catch (IllegalArgumentException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
    
    /**
     * Creates a {@code Processors}.
     * 
     * @return a Processors
     * @throws MojoFailureException if retrieving the compile classpath elements failed
     */
    protected Processors processors() throws MojoFailureException {
        try {
            return Processors.simple(getLog(), name, project.getCompileClasspathElements(), version, override);
            
        } catch (DependencyResolutionRequiredException e) {
            throw new MojoFailureException(e.getMessage(), e);
        }
    }
    
}
