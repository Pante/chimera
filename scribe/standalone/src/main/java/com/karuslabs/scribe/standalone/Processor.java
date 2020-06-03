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
package com.karuslabs.scribe.standalone;


import com.google.auto.service.AutoService;

import com.karuslabs.annotations.processor.AnnotationProcessor;
import com.karuslabs.scribe.core.Environment;

import java.util.*;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;


@AutoService(javax.annotation.processing.Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedAnnotationTypes("com.karuslabs.scribe.annotations.*")
public class Processor extends AnnotationProcessor {
    
    Environment<Element> environment;
    StandaloneProcessor processor;
    StandaloneYAML yaml;
    
    
    @Override
    public void init(ProcessingEnvironment env) {
        super.init(env);
        environment = new StandaloneEnvironment(env.getMessager());
        processor = new StandaloneProcessor(environment, elements, types);
        yaml = new StandaloneYAML(env.getFiler(), messager);
    }
    
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment round) {
        if (round.getElementsAnnotatedWithAny(processor.annotations()).isEmpty()) {
            return false;
        }
        
        processor.initialize(round);
        processor.run();
        
        if (!round.processingOver()) {
            yaml.write(environment.mappings);
        }
        
        return false;
    }
    
}
