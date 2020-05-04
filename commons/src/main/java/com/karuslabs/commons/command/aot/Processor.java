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
package com.karuslabs.commons.command.aot;

import com.google.auto.service.AutoService;

import com.karuslabs.annotations.processor.AnnotationProcessor;
import com.karuslabs.commons.command.aot.analyzers.Analyzer;
import com.karuslabs.commons.command.aot.annotations.*;
import com.karuslabs.commons.command.aot.generation.*;
import com.karuslabs.commons.command.aot.generation.blocks.*;
import com.karuslabs.commons.command.aot.lexers.*;
import com.karuslabs.commons.command.aot.parsers.*;
import com.karuslabs.commons.command.aot.resolvers.*;

import java.lang.annotation.Annotation;
import java.util.*;
import javax.annotation.processing.*;
import javax.lang.model.element.TypeElement;

import static javax.lang.model.SourceVersion.RELEASE_11;


@AutoService(javax.annotation.processing.Processor.class)
@SupportedSourceVersion(RELEASE_11)
@SupportedAnnotationTypes("com.karuslabs.commons.command.aot.annotations.*")
public class Processor extends AnnotationProcessor {
    
    Environment environment;
    Parser command;
    Parser bind;
    Analyzer analyzer;
    Packager packager;
    Generator generator;
    
    
    @Override
    public void init(ProcessingEnvironment env) {
        var lexer = new CommandLexer(new LiteralLexer(), new ArgumentLexer());
        
        environment = new Environment(env.getMessager(), env.getFiler(), env.getElementUtils(), env.getTypeUtils());
        command = new CommandParser(environment, lexer);
        bind = new BindParser(environment, lexer, new MethodResolver(environment), new VariableResolver(environment));
        analyzer = new Analyzer(environment);
        packager = new Packager(environment);
        generator = new Generator(environment, packager, new TypeBlock(), new MethodBlock());
    }
    
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        pack(env);
        parse(command, annotations, env, Command.class);
        parse(bind, annotations, env, Bind.class);
        analyzer.analyze();
        
        if (!environment.error()) {
            generator.generate();
        }
        
        environment.clear();
        return false;
    }
    
    
    void pack(RoundEnvironment env) {
        var elements = env.getElementsAnnotatedWith(Pack.class);
        if (elements.isEmpty()) {
            environment.error("Project must contain at least one @Pack annotation");
            
        } else if (elements.size() > 1) {
            for (var element : elements) {
                environment.error(element, "Project must contain only one @Pack annotation");
            }
            
        } else {
            packager.resolve(elements.toArray(ARRAY)[0]);
        }
    }
    
    void parse(Parser parser, Set<? extends TypeElement> annotations, RoundEnvironment env, Class<? extends Annotation> annotation) {
        for (var element : env.getElementsAnnotatedWith(annotation)) {
            parser.parse(element);
        }
    }
    
}
