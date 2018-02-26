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
package com.karuslabs.spigot.plugin.descriptor;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.*;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import static org.apache.maven.plugins.annotations.LifecyclePhase.COMPILE;
import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;


@Mojo(name = "descriptor", defaultPhase = COMPILE, threadSafe = false)
public class DescriptorMojo extends AbstractMojo {

    @Parameter(name = "directory", defaultValue = "${project}", readonly = true, required = true)
    protected MavenProject project;
    
    @Parameter(name = "plugin-yaml", defaultValue = "${project.basedir}/src/main/resources/plugin.yml")
    protected File file;
    
    @Parameter
    protected @Nullable String main;
    
    @Parameter(defaultValue = "${project.version}", required = true)
    protected String version;
    
    @Parameter(defaultValue = "true", required = true)
    protected boolean override;
    

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            Set<URL> urls = new HashSet<>();
            List<String> elements  = project.getCompileClasspathElements();
            for (String element : elements) {
                urls.add(new File(element).toURI().toURL());
            }
            ClassLoader loader = URLClassLoader.newInstance(urls.toArray(new URL[0]), Thread.currentThread().getContextClassLoader());
            
            Reflections reflections = new Reflections(loader);
            
            getLog().info(reflections.getSubTypesOf(Plugin.class).toString());
            
        } catch (DependencyResolutionRequiredException e) {
            throw new MojoExecutionException("Error while determining the classpath elements", e);
        } catch (MalformedURLException ex) {
            Logger.getLogger(DescriptorMojo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public MavenProject getProject() {
        return project;
    }

    public File getFile() {
        return file;
    }

    public String getMain() {
        return main;
    }

    public String getVersion() {
        return version;
    }
    
    public boolean isOverriding() {
        return override;
    }
    
}
