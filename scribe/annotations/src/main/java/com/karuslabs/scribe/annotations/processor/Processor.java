/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
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
package com.karuslabs.scribe.annotations.processor;

import com.google.auto.service.AutoService;

import com.karuslabs.annotations.processors.AnnotationProcessor;
import com.karuslabs.scribe.annotations.*;
import com.karuslabs.scribe.annotations.resolvers.*;

import java.lang.annotation.Annotation;
import java.util.*;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;


/**
 * A processor that builds a {@code plugin.yml} using annotations in {@link com.karuslabs.scribe.annotations}.
 */
@AutoService(javax.annotation.processing.Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedAnnotationTypes("com.karuslabs.scribe.annotations.*")
public class Processor extends AnnotationProcessor {
    
    Map<Class<? extends Annotation>, Resolver> resolvers;
    YAMLWriter writer;
    
    
    /**
     * Creates a {@code Processor}.
     */
    public Processor() {
        resolvers = new HashMap<>();
    }
    
    
    @Override
    public void init(ProcessingEnvironment environment) {
        super.init(environment);
        var commandResolver = new CommandResolver(messager);
        var permissionResolver = new PermissionResolver(messager);
        
        resolvers.put(API.class, new APIResolver(messager));
        resolvers.put(Command.class, commandResolver);
        resolvers.put(Commands.class, commandResolver);
        resolvers.put(Information.class, new InformationResolver(messager));
        resolvers.put(Load.class, new LoadResolver(messager));
        resolvers.put(Permission.class, permissionResolver);
        resolvers.put(Permissions.class, permissionResolver);
        resolvers.put(Plugin.class, new PluginResolver(elements, types, messager));
        
        writer = new YAMLWriter(environment.getFiler(), messager);
    }
    
    
    /**
     * Delegates processing of the given annotations to a resolver and builds a
     * {@code plugin.yml}.
     * 
     * @param annotations the annotations used to build a {@code plugin.yml}
     * @param environment the environment
     * @return {@code false}
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment environment) {
        if (environment.getElementsAnnotatedWithAny(resolvers.keySet()).isEmpty()) {
            return false;
        }
        
        var results = new HashMap<String, Object>();
        
        for (var entry : resolvers.entrySet()) {
            entry.getValue().resolve(environment.getElementsAnnotatedWith(entry.getKey()), results);
        }
        
        if (!environment.processingOver()) {
            writer.write(results);
        }
        
        return false;
    }
    
}
