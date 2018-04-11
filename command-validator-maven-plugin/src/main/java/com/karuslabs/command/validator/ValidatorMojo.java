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
package com.karuslabs.command.validator;

import com.karuslabs.commons.command.*;
import com.karuslabs.commons.command.annotation.resolvers.CommandResolver;
import com.karuslabs.commons.command.completion.Completion;
import com.karuslabs.commons.command.parser.*;
import com.karuslabs.commons.configuration.Configurations;
import com.karuslabs.commons.locale.MessageTranslation;
import com.karuslabs.commons.locale.providers.Provider;

import java.io.*;

import org.apache.maven.plugin.*;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import org.bukkit.configuration.ConfigurationSection;

import static java.util.Collections.EMPTY_SET;
import static org.apache.maven.plugins.annotations.LifecyclePhase.COMPILE;


@Mojo(name = "validate-commands-yml", defaultPhase = COMPILE)
public class ValidatorMojo extends AbstractMojo {
    
    @Parameter(defaultValue = "${project.basedir}/src/main/resources/commands.yml")
    protected File file;
    
    @Parameter(defaultValue = "WARNING", property = "parser.validation")
    protected Validation validation;
    
    @Parameter
    protected String[] commands = new String[] {};
    
    @Parameter
    protected String[] completions = new String[] {};
    
    @Parameter
    protected String[] translations = new String[] {};
  
    
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Loading configuration file: " + file.getPath());
        ConfigurationSection config = loadConfiguration();
       
        getLog().info("Loading references specified in project pom.xml");
        References references = loadReferences();
        Parser parser = loadParser(references);
        
        try {
            getLog().info("Validating configuration file, " + validation.getDescription());
            parser.parse(config);
            getLog().info("Found no errors in configuration file");
            
        } catch (ParserException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
    
    
    protected ConfigurationSection loadConfiguration() throws MojoExecutionException {
        try {
            return Configurations.from(new BufferedInputStream(new FileInputStream(file)));
            
        } catch (FileNotFoundException | IllegalArgumentException | NullPointerException | UncheckedIOException e) {
            throw new MojoExecutionException("Invalid file specified: \"" + file.getPath() + "\", file must be a YAML file.");
        }
    }
    
    
    protected References loadReferences() {
        References references = new References();
        for (String command : commands) {
            references.command(command, Command.NONE);
        }
        
        for (String completion : completions) {
            references.completion(completion, Completion.NONE);
        }
        
        for (String translation : translations) {
            references.translation(translation, MessageTranslation.NONE);
        }
        
        return references;
    }
    
    
    protected Parser loadParser(References references) {
        NullHandle handle;
        switch (validation) {
            case LENIENT:
                handle = NullHandle.NONE;
                break;
                
            case WARNING:
                handle = (config, key, value) -> getLog().warn("Unresolvable reference: \"" + value + "\" at: \"" + config.getCurrentPath() + "." + key + "\"");
                break;
                
            default:
                handle = (config, key, value) -> {
                    String message = "Invalid reference: \"" + value + "\" at: \"" + config.getCurrentPath() + "." + key + "\", reference must either be registered or point to a assignable value";
                    getLog().error(message);
                    throw new ParserException(message);
                };
                break;
        }
        
        return Parsers.newParser(null, new CommandResolver(null, EMPTY_SET), null, references, handle, Provider.NONE);
    }
    
}
