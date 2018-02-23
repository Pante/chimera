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
package com.karuslabs.configuration.validator.maven.plugin;

import com.karuslabs.commons.command.*;
import com.karuslabs.commons.command.completion.Completion;
import com.karuslabs.commons.command.parser.*;
import com.karuslabs.commons.configuration.Configurations;
import com.karuslabs.commons.locale.MessageTranslation;

import java.io.File;
import java.net.URISyntaxException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import org.apache.maven.plugin.logging.Log;

import org.bukkit.configuration.ConfigurationSection;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ValidatorMojoTest {
    
    static final ConfigurationSection TEST = Configurations.from(ValidatorMojoTest.class.getClassLoader().getResourceAsStream("test.yml"));
    
    ValidatorMojo validator = spy(new ValidatorMojo());
    Log log = mock(Log.class);
    
    
    @Test
    void execute() throws MojoExecutionException, MojoFailureException, URISyntaxException {
        validator.file = new File(getClass().getClassLoader().getResource("test.yml").toURI());
        validator.validation = Validation.LENIENT;
        
        doReturn(log).when(validator).getLog();
        
        validator.execute();
        
        verify(log).info("Loading configuration file: " + validator.file.getPath());
        verify(validator).loadConfiguration();
        verify(log).info("Loading references specified in project pom.xml");
        verify(validator).loadReferences();
        verify(validator).loadParser(any(References.class));
        verify(log).info("Validating configuration file, " + validator.validation.getDescription());
        verify(log).info("Found no errors in configuration file");
    }
    
    
    @Test
    void execute_ThrowsException() throws URISyntaxException, MojoExecutionException, MojoFailureException {
        validator.file = new File(getClass().getClassLoader().getResource("invalid.yml").toURI());
        validator.validation = Validation.STRICT;
        assertEquals(
            "Invalid reference: \"lol\" at: \"commands.command\", reference must either be registered or point to a assignable value", 
            assertThrows(MojoExecutionException.class, validator::execute).getMessage()
        );
    }
    
    
    @Test
    void loadConfiguration() throws MojoExecutionException, URISyntaxException {
        validator.file = new File(getClass().getClassLoader().getResource("test.yml").toURI());
        assertEquals("a", validator.loadConfiguration().getString("path"));
    }
    
    
    @Test
    void loadConfiguration_ThrowsException() throws URISyntaxException {
        validator.file = new File(getClass().getClassLoader().getResource("invalid.xml").toURI());
        assertEquals(
            "Invalid file specified: \"" + validator.file.getPath() + "\", file must be a YAML file.",
            assertThrows(MojoExecutionException.class, validator::loadConfiguration).getMessage()
        );
    }
    
    
    @Test
    void loadReferences() {
        validator.commands = new String[] {"A"};
        validator.completions = new String[] {"B"};
        validator.translations = new String[] {"C"};
        
        References references = validator.loadReferences();
        
        assertSame(Command.NONE, references.getCommand("A"));
        assertSame(Completion.NONE, references.getCompletion("B"));
        assertSame(MessageTranslation.NONE, references.getTranslation("C"));
    }
    
    
    @Test
    void loadParser_LENIENT() {
        validator.validation = Validation.LENIENT;
        
        assertSame(NullHandle.NONE, ((ReferableToken<Command>) validator.loadParser(null).getToken()).getHandle());
    }
    
    
    @Test
    void loadParser_WARNING() {
        validator.validation = Validation.WARNING;
        doReturn(log).when(validator).getLog();
        
        ((ReferableToken<Command>) validator.loadParser(null).getToken()).getHandle().handle(TEST, "key", "value");
        
        verify(log).warn("Unresolvable reference: \"value\" at: \".key\"");
    }
    
    
    @Test
    void loadParser_STRICT() {
        validator.validation = Validation.STRICT;
        doReturn(log).when(validator).getLog();
        
        String message = "Invalid reference: \"value\" at: \".key\", reference must either be registered or point to a assignable value";
        
        assertEquals(
            message, 
            assertThrows(ParserException.class, () -> ((ReferableToken<Command>) validator.loadParser(null).getToken()).getHandle().handle(TEST, "key", "value")).getMessage()
        );
        verify(log).error(message);
    }
    
}
