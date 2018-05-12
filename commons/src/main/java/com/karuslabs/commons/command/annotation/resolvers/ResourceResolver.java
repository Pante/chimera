/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
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
package com.karuslabs.commons.command.annotation.resolvers;

import com.karuslabs.commons.command.*;
import com.karuslabs.commons.locale.*;
import com.karuslabs.commons.locale.annotation.*;

import java.lang.reflect.AnnotatedElement;


public class ResourceResolver implements Resolver {

    @Override
    public void resolve(AnnotatedElement element, CommandExecutor executor, Command... commands) {
        MessageTranslation translation = translation(element, executor);
        for (Command command : commands) {
            command.setTranslation(translation);
        }
    }
    
    protected MessageTranslation translation(AnnotatedElement element, CommandExecutor executor) {
        EmbeddedResources embedded = element.getAnnotation(EmbeddedResources.class);
        ExternalResources external = element.getAnnotation(ExternalResources.class);
        
        Builder<MessageTranslation> builder = MessageTranslation.builder().bundle(element.getAnnotation(Bundle.class).value());
        if (embedded != null) {
            for (String resource : embedded.value()) {
                builder.embedded(resource);
            }
        }
        
        if (external != null) {
            for (String resource : external.value()) {
                builder.external(resource);
            }
        }
        
        return builder.build();
    }

    @Override
    public boolean isResolvable(AnnotatedElement element) {
        return element.getAnnotation(Bundle.class) != null && (element.getAnnotation(EmbeddedResources.class) != null || element.getAnnotation(ExternalResources.class) != null);
    }
    
}
