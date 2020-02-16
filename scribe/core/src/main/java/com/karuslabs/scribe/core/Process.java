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

import java.io.UncheckedIOException;
import java.lang.annotation.Annotation;
import java.net.*;
import java.util.*;
import java.util.stream.Stream;

import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.stream.Collectors.*;


public abstract class Process<T> {
    
    public static ClassLoader loader(List<String> classpaths) {
        return URLClassLoader.newInstance(
            classpaths.stream().map(classpath -> {
                try {
                    return new URL(classpath);
                } catch (MalformedURLException e) {
                    throw new UncheckedIOException(e);
                }
            }).toArray(URL[]::new),
            Process.class.getClassLoader()
        );
    } 
    
    
    Project project;
    Extractor<T> extractor;
    List<Resolver<T>> resolvers;
    @Nullable Set<Class<? extends Annotation>> annotations;
    
    
    public Process(Project project, Extractor<T> extractor, PluginResolver<T> resolver) {
        this(project, extractor, List.of(
            resolver,
            new APIResolver(),
            new CommandResolver(),
            new InformationResolver(),
            new LoadResolver(),
            new PermissionResolver()
        ));
    }
    
    public Process(Project project, Extractor<T> extractor, List<Resolver<T>> resolvers) {
        this.project = project;
        this.extractor = extractor;
        this.resolvers = resolvers;
    }
    
    
    
    public Resolution run() {
        var resolution = new Resolution<T>();
        for (var resolver : resolvers) {
            var types = resolver.annotations().stream().collect(flatMapping(this::annotated, toSet()));
            resolver.initialize(project, extractor, resolution);
            resolver.resolve(types);
        }
        
        return resolution;
    }
    
    protected abstract Stream<T> annotated(Class<? extends Annotation> annotation);
    
    
    public Set<Class<? extends Annotation>> annotations() {
        if (annotations == null) {
            annotations = resolvers.stream().collect(flatMapping((resolver) -> resolver.annotations().stream(), toSet()));
        }
        
        return annotations;
    }
    
}
