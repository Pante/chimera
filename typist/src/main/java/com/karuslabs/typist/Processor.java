/*
 * The MIT License
 *
 * Copyright 2021 Karus Labs.
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
package com.karuslabs.typist;

import com.google.auto.service.AutoService;

import com.karuslabs.annotations.Lazy;
import com.karuslabs.typist.Binding.Pattern;
import com.karuslabs.typist.generation.Generation;
import com.karuslabs.typist.generation.chunks.*;
import com.karuslabs.typist.lexers.*;
import com.karuslabs.typist.lexers.Lexer.Memoizer;
import com.karuslabs.typist.lints.*;
import com.karuslabs.typist.parsers.*;
import com.karuslabs.utilitary.AnnotationProcessor;

import java.util.*;
import javax.annotation.processing.*;
import javax.lang.model.element.*;

import static javax.lang.model.SourceVersion.RELEASE_16;

/**
 * An annotation processor that generates Java code which initializes Brigadier commands
 * from annotations in {@link com.karuslabs.typist.annotations}.
 */
@AutoService(javax.annotation.processing.Processor.class)
@SupportedSourceVersion(RELEASE_16)
@SupportedAnnotationTypes("com.karuslabs.typist.annotations.*")
public class Processor extends AnnotationProcessor {
    
    @Lazy Environment environment;
    @Lazy Types types;
    @Lazy Parser[] parsers;
    @Lazy Lint[] lints;
    @Lazy Generation generation;
    boolean processed;
    
    @Override
    public void init(ProcessingEnvironment environment) {
        super.init(environment);
        types = new Types(elements, super.types);
        parsers();
        lints();
        generation(environment);
    }
    
    void parsers() {
        var memoizer = new Memoizer();
        // ArgumentLexer && LiteralLexer CANNOT share memoizers since memoization is done using names only.
        var argumentLexer = new ArgumentLexer();
        parsers = new Parser[] {
            new CommandParser(logger, new CommandLexer(argumentLexer, LiteralLexer.aliasable(memoizer))),
            new BindParser(new BindParser.Visitor(types), logger, new CommandLexer(argumentLexer, LiteralLexer.single(memoizer))),
            new LetParser(logger, argumentLexer),
            new PackageParser(logger),
        };
    }
    
    void lints() {
        lints = new Lint[] {
            new ArgumentPositionLint(logger),
            new ConflictingAliasLint(logger),
            new DuplicateCommandLint(logger),
            new ReferenceTypeLint(logger, types),
            new TreeLint(logger,
                new ArgumentTypeLint(logger, types),
                new BindingPatternLint(logger),
                new MethodSignatureLint(logger, types),
                new PublicFinalBindingLint(logger, types)
            ),
        };
    }
    
    void generation(ProcessingEnvironment environment) {
        var counter = new int[] {0}; // we use an array instead of int to simulate a ref value
        generation = new Generation(logger, environment.getFiler(),
            new Header(),
            new Type(
                new MethodBody(
                    new CommandInstantiation(Map.of(
                        Pattern.COMMAND, Lambda.command(types, counter),
                        Pattern.EXECUTION, Lambda.execution(types, counter),
                        Pattern.REQUIREMENT, Lambda.requirement(types, counter),
                        Pattern.SUGGESTION_PROVIDER, Lambda.suggestionProvider(types, counter)
                    ), counter)
                )
            ),
            counter
        );
    }
    
    @Override
    public boolean process(Set<? extends TypeElement> types, RoundEnvironment round) {
        if (processed) {
            return false;
        }
        
        environment = new Environment();
        
        for (var parser : parsers) {
            parser.parse(environment, round.getElementsAnnotatedWith(parser.annotation()));
        }
        
        Lint.visit(environment, lints);
        
        if (!logger.error()) {
            generation.generate(environment);
        }
        
        logger.clear();
        environment = null;
        processed = true;
        
        return false;
    }

}
