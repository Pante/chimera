package com.karuslabs.test.maven.plugin;

import org.apache.maven.plugin.*;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;

@Mojo(name = "scribe", defaultPhase = LifecyclePhase.COMPILE, threadSafe = false, requiresDependencyResolution = ResolutionScope.COMPILE)
public class ScribeMojo extends AbstractMojo {    
    
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    MavenProject pom;
    

    @Override
    public void execute() throws MojoFailureException {
        System.out.println("System ❤");
        getLog().
        getLog().error("Logger ❤");
        getLog().error(new IllegalArgumentException("Logger exception ❤"));
        throw new MojoFailureException("Exception ❤");
    }
    
}