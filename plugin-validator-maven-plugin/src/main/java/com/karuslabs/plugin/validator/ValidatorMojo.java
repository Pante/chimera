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
package com.karuslabs.plugin.validator;

import java.io.*;
import java.util.List;

import org.apache.maven.plugin.*;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import org.bukkit.configuration.file.YamlConfiguration;

import static org.apache.maven.plugins.annotations.LifecyclePhase.COMPILE;


/**
 * 
 */
@Mojo(name = "validator", defaultPhase = COMPILE, threadSafe = false)
public class ValidatorMojo extends AbstractMojo {
    
    /**
     * The classpath elements which defaults to {@literal ${project.compileClasspathElements}}.
     */
    @Parameter(defaultValue = "${project.compileClasspathElements}", readonly = true, required = true)
    protected List<String> elements;
    
    /**
     * The location of the plugin.yml which defaults to {@literal ${project.basedir}/src/main/resources/plugin.yml}.
     */
    @Parameter(defaultValue = "${project.basedir}/src/main/resources/plugin.yml")
    protected File file;
    
    
    /**
     * Executes this {@code ValidatorMojo}.
     * 
     * @throws MojoExecutionException if this ValidatorMojo fails to validate the plugin.yml
     * @throws MojoFailureException if an internal exception occurs
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Loading plugin.yml: " + file.getPath());
        YamlConfiguration config = loadConfiguration();
        
        getLog().info("Loading validators");
        Validators validators = validators();
        
        getLog().info("Validating plugin.yml");
        validators.validate(config);
        getLog().info("Found no errors in plugin.yml");
    }
    
    /**
     * Loads the plugin.yml specified in the pom.xml.
     * 
     * @return the plugin.yml
     * @throws MojoExecutionException if the plugin.yml could not be loaded
     */
    protected YamlConfiguration loadConfiguration() throws MojoExecutionException {
        try {
            return YamlConfiguration.loadConfiguration(file);
            
        } catch (IllegalArgumentException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
    
    /**
     * Creates a {@code Validators}.
     * 
     * @return a Validators
     */
    protected Validators validators() {
        return Validators.simple(getLog(), elements);
    }
    
}
