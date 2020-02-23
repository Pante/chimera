/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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
package com.karuslabs.scribe.maven.plugin;

import com.karuslabs.scribe.core.Message;

import java.io.File;
import java.util.List;

import org.apache.maven.model.*;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ScribeMojoTest {
    
    MavenProject pom = mock(MavenProject.class);
    Log log = mock(Log.class);
    File folder = new File(getClass().getClassLoader().getResource("beacon.yml").getFile()).getParentFile();
    File file = new File(folder, "plugin.yml");
    ScribeMojo mojo;
    
    
    @BeforeEach
    void before() {
        mojo = spy(new ScribeMojo());
        mojo.pom = pom;
        mojo.classpaths = List.of();
        mojo.folder = folder;
        mojo.setLog(log);
    }
    
    
    @Test
    void execute() throws MojoFailureException {
        doReturn(List.of()).when(mojo).log(any(List.class));
        
        mojo.execute();
        
        assertTrue(file.exists());
    }
    
    
    @Test
    void execute_throws_exception() throws MojoFailureException {
        assertEquals("Resolution failure", assertThrows(MojoFailureException.class, mojo::execute).getMessage());
    }
    
    
    @Test
    void from() {
        var contributor = new Contributor();
        contributor.setName("bob");
        
        var developer = new Developer();
        developer.setName("bobby");
        
        var dependency = new Dependency();
        dependency.setGroupId("org.spigotmc");
        dependency.setArtifactId("spigot-api");
        dependency.setVersion("1.15-R0.1-SNAPSHOT");
        
        when(pom.getName()).thenReturn("name");
        when(pom.getVersion()).thenReturn("version");
        when(pom.getDescription()).thenReturn("description");
        when(pom.getUrl()).thenReturn("url");
        when(pom.getContributors()).thenReturn(List.of(contributor));
        when(pom.getDevelopers()).thenReturn(List.of(developer));
        when(pom.getDependencies()).thenReturn(List.of(dependency));
        
        var project = mojo.project();
        
        assertEquals("name", project.name);
        assertEquals("version", project.version);
        assertEquals("description", project.description);
        assertEquals("url", project.url);
        assertEquals("1.15-R0.1-SNAPSHOT", project.api);
        assertEquals(List.of("bob", "bobby"), project.authors);
    }
    
    
    @Test
    void log() {
        var errors = mojo.log(List.of(Message.warning(null, "first"), Message.error(null, "second")));
        
        verify(log).warn("first");
        verify(log).error("second");
        
        assertEquals(1, errors.size());
    }
        
    
    @AfterEach
    void after() {
        if (file.exists()) {
            file.delete();
        }
    }
    
} 
