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
package com.karuslabs.scribe.core;

import com.karuslabs.scribe.core.resolvers.*;

import java.io.*;
import java.lang.annotation.Annotation;
import java.net.*;
import java.util.*;
import java.util.stream.Stream;

import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.stream.Collectors.*;


/**
 * A processor that builds a plugin.yml using annotations in {@link com.karuslabs.scribe.annotations}.
 * 
 * @param <T> the annotated type
 */
public abstract class Processor<T> {
    
    /**
     * Returns a {@code ClassLoader} with the given classpaths.
     * 
     * @param classpaths the classpaths.
     * @return a ClassLoader
     */
    public static ClassLoader loader(List<String> classpaths) {
        return URLClassLoader.newInstance(classpaths.stream().map(classpath -> {
                try {
                    return new File(classpath).toURI().toURL();
                } catch (MalformedURLException e) {
                    throw new UncheckedIOException(e);
                }
            }).toArray(URL[]::new),
            Processor.class.getClassLoader()
        );
    } 
    
    
    Project project;
    Extractor<T> extractor;
    List<Resolver<T>> resolvers;
    @Nullable Set<Class<? extends Annotation>> annotations;
    
    
    /**
     * Creates a {@code Processor} with the given parameters and all {@code Resolvers}
     * except {@link PluginResolver}.
     * 
     * @param project the project
     * @param extractor the extractor
     * @param resolver the resolver
     */
    public Processor(Project project, Extractor<T> extractor, PluginResolver<T> resolver) {
        this(project, extractor, List.of(
            resolver,
            new APIResolver(),
            new CommandResolver(),
            new InformationResolver(),
            new LoadResolver(),
            new PermissionResolver()
        ));
    }
    
    /**
     * Creates a {@code Processor} with the given parameters.
     * 
     * @param project the project
     * @param extractor the extractor
     * @param resolvers a list of {@code Resolvers}.
     */
    public Processor(Project project, Extractor<T> extractor, List<Resolver<T>> resolvers) {
        this.project = project;
        this.extractor = extractor;
        this.resolvers = resolvers;
    }
    
    
    
    /**
     * Resolves all supported annotations on the types returned by {@link #annotated(Class)}.
     * 
     * @return a {@code Resolution} that represents the resolution of all supported annotations
     */
    public Resolution<T> run() {
        var resolution = new Resolution<T>();
        for (var resolver : resolvers) {
            var types = resolver.annotations().stream().collect(flatMapping(this::annotated, toSet()));
            resolver.initialize(project, extractor, resolution);
            resolver.resolve(types);
        }
        
        return resolution;
    }
    
    /**
     * Returns all types annotated with {@code annotation}.
     * 
     * @param annotation the annotation
     * @return a stream of all types annotated with {@code annotation}
     */
    protected abstract Stream<T> annotated(Class<? extends Annotation> annotation);
    
    
    /**
     * Returns all annotations supported by this {@code Processor}.
     * 
     * @return all supported annotations
     */
    public Set<Class<? extends Annotation>> annotations() {
        if (annotations == null) {
            annotations = resolvers.stream().collect(flatMapping((resolver) -> resolver.annotations().stream(), toSet()));
        }
        
        return annotations;
    }
    
}
