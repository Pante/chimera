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
package com.karuslabs.commons.command.aot.old;

import com.karuslabs.commons.command.aot.lexers.LiteralLexer;
import com.karuslabs.commons.command.aot.lexers.CommandLexer;
import com.karuslabs.commons.command.aot.lexers.ArgumentLexer;
import com.karuslabs.commons.command.aot.Command;
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
    SourceResolver source;
    Generator generator;
    boolean processed;
    
    
    @Override
    public void init(ProcessingEnvironment env) {
        super.init(env);
        
        var lexer = new CommandLexer(new ArgumentLexer(), new LiteralLexer());
        
        environment = new Environment(env.getMessager(), env.getFiler(), env.getElementUtils(), env.getTypeUtils());
        command = new CommandParser(environment, lexer);
        bind = new BindParser(environment, lexer, new MethodResolver(environment), new VariableResolver(environment));
        analyzer = new Analyzer(environment);
        source = new SourceResolver(environment);
        generator = new Generator(environment, source, new TypeBlock(), new MethodBlock());
        processed = false;
    }
    
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment round) {
        if (processed) {
            return false;
        }
        
        resolveSource(round.getElementsAnnotatedWith(Source.class));
        parse(command, round, Command.class);
        parse(bind, round, Bind.class);
        
        analyzer.analyze();
        
        if (!environment.error()) {
            generator.generate();
        }
        
        environment.clear();
        processed = true;
        return false;
    }
    
    
    void resolveSource(Set<? extends Element> elements) {
        if (elements.size() == 1) {
            source.resolve(elements.toArray(new Element[0])[0]);
            
        } else if (elements.isEmpty()) {
            error("Project does not contain a @Source annotation, should contain one @Source annotation");
            
        } else if (elements.size() > 1) {
            for (var element : elements) {
                error(element, "Project contains " + elements.size() + " @Source annotations, should contain one @Source annotation");
            }
        }
    }
    
    
    void parse(Parser parser, RoundEnvironment round, Class<? extends Annotation> annotation) {
        for (var element : round.getElementsAnnotatedWith(annotation)) {
            parser.parse(element);
        }
    }
    
}
