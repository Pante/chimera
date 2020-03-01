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

import java.util.*;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;


@AutoService(javax.annotation.processing.Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedAnnotationTypes("com.karuslabs.scribe.annotations.*")
public class Processor extends AnnotationProcessor {
    
    StandaloneProcessor processor;
    StandaloneYAML yaml;
    
    
    @Override
    public void init(ProcessingEnvironment environment) {
        super.init(environment);
        processor = new StandaloneProcessor(elements, types);
        yaml = new StandaloneYAML(environment.getFiler(), messager);
    }
    
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment environment) {
        if (environment.getElementsAnnotatedWithAny(processor.annotations()).isEmpty()) {
            return false;
        }
        
        processor.initialize(environment);
        var resolution = processor.run();
        if (!environment.processingOver()) {
            yaml.write(resolution.mappings);
            for (var message : resolution.messages) {
                switch (message.type) {
                    case ERROR:
                        error(message.location, message.value);
                        break;

                    case WARNING:
                        warn(message.location, message.value);
                        break;

                    case INFO:
                        note(message.location, message.value);
                        break;

                    default:
                        throw new UnsupportedOperationException("Unsupported type: " + message.type);
                }
            }
        }
        
        return false;
    }
    
}
