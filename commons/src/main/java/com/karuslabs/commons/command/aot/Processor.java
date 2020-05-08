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
import com.karuslabs.commons.command.aot.annotations.*;
import com.karuslabs.commons.command.aot.generation.*;
import com.karuslabs.commons.command.aot.generation.blocks.*;
import com.karuslabs.commons.command.aot.lexers.*;
import com.karuslabs.commons.command.aot.parsers.*;
import com.karuslabs.commons.command.aot.resolvers.*;

import java.lang.annotation.Annotation;
import java.util.*;
import javax.annotation.processing.*;
import javax.lang.model.element.*;

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
    boolean compiled;
    
    
    @Override
    public void init(ProcessingEnvironment env) {
        super.init(env);
        
        var lexer = new CommandLexer(new ArgumentLexer(), new LiteralLexer());
        
        environment = new Environment(env.getMessager(), env.getFiler(), env.getElementUtils(), env.getTypeUtils());
        command = new CommandParser(environment, lexer);
        bind = new BindParser(environment, lexer, new MethodResolver(environment), new VariableResolver(environment));
        analyzer = new Analyzer(environment);
        packager = new Packager(environment);
        generator = new Generator(environment, packager, new TypeBlock(), new MethodBlock());
        compiled = false;
    }
    
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment round) {
        if (compiled) {
            return false;
        }
        
        resolveFile(round.getElementsAnnotatedWith(Emit.class));
        parse(command, round, Command.class);
        parse(bind, round, Bind.class);
        analyzer.analyze();
        
        if (!environment.error()) {
            generator.generate();
        }
        
        environment.clear();
        compiled = true;
        return false;
    }
    
    
    void resolveFile(Set<? extends Element> elements) {
        if (elements.size() == 1) {
            packager.resolve(elements.toArray(new Element[0])[0]);
            
        } else if (elements.isEmpty()) {
            error("Project does not contain a @Pack annotation, should contain one @Pack annotation");
            
        } else if (elements.size() > 1) {
            for (var element : elements) {
                error(element, "Project contains " + elements.size() + " @Pack annotations, should contain one @Pack annotation");
            }
        }
    }
    
    
    void parse(Parser parser, RoundEnvironment env, Class<? extends Annotation> annotation) {
        for (var element : env.getElementsAnnotatedWith(annotation)) {
            parser.parse(element);
        }
    }
    
}
