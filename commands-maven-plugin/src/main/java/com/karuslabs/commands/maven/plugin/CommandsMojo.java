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
package com.karuslabs.commands.maven.plugin;

import com.karuslabs.commons.command.old.CompletionsElement;
import com.karuslabs.commons.command.old.TranslationElement;
import com.karuslabs.commons.command.old.CommandElement;
import com.karuslabs.commons.command.old.CommandsElement;
import com.karuslabs.commons.command.old.Parser;
import com.karuslabs.commons.command.parser.ParserException;
import com.karuslabs.commons.command.old.CompletionElement;
import com.karuslabs.commons.configuration.Configurations;
import com.karuslabs.commons.locale.providers.Provider;

import java.io.*;

import org.apache.maven.plugin.*;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import org.bukkit.configuration.ConfigurationSection;

import static org.apache.maven.plugins.annotations.LifecyclePhase.COMPILE;


@Mojo(name = "commands", defaultPhase = COMPILE, threadSafe = false)
public class CommandsMojo extends AbstractMojo {
    
    @Parameter(defaultValue = "${project.basedir}/src/main/resources/commands.yml", readonly = true)
    protected File file;
    
    
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        ConfigurationSection config = loadConfiguration();
        try {
            loadParser().parse(config);
            
        } catch (ParserException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
    
    protected ConfigurationSection loadConfiguration() throws MojoExecutionException {
        try {
            return Configurations.from(new BufferedInputStream(new FileInputStream(file)));
            
        } catch (FileNotFoundException | IllegalArgumentException | UncheckedIOException e) {
            throw new MojoExecutionException("Invalid file specified:" + file.getPath() + ", file must be a YAML file.");
        }
    }
    
    protected Parser loadParser() {
        TranslationElement translation = new TranslationElement(null, Provider.NONE);
        CompletionElement completion = new CompletionElement();
        CompletionsElement completions = new CompletionsElement(completion);
        CommandsElement commands = new CommandsElement(null);
        CommandElement command = new CommandElement(null, commands, translation, completions);
        commands.setCommandElement(command);
        
        return new Parser(command, translation, completion);
    }
    
}
