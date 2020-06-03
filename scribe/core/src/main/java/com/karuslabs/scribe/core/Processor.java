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

import com.karuslabs.scribe.core.parsers.*;

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
    
    
    Environment<T> environment;
    List<Parser<T>> parsers;
    @Nullable Set<Class<? extends Annotation>> annotations;
    
    
    /**
     * Creates a {@code Processor} with the given parameters and all {@code Parser}s
     * except {@link PluginParser}.
     * 
     * @param environment the environment
     * @param parser the parser
     */
    public Processor(Environment<T> environment, PluginParser<T> parser) {
        this(environment, List.of(
            parser,
            new APIParser(environment),
            new CommandParser(environment),
            new InformationParser(environment),
            new LoadParser(environment),
            new PermissionParser(environment)
        ));
    }
    
    /**
     * Creates a {@code Processor} with the given parameters.
     * 
     * @param environment the environment
     * @param parsers a list of {@code Parser}s.
     */
    public Processor(Environment<T> environment, List<Parser<T>> parsers) {
        this.environment = environment;
        this.parsers = parsers;
    }
    
    
    /**
     * Resolves all supported annotations on the types returned by {@link #annotated(Class)}.
     */
    public void run() {
        for (var parser : parsers) {
            var types = parser.annotations().stream().collect(flatMapping(this::annotated, toSet()));
            parser.parse(types);
        }
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
            annotations = parsers.stream().collect(flatMapping((resolver) -> resolver.annotations().stream(), toSet()));
        }
        
        return annotations;
    }
    
}
