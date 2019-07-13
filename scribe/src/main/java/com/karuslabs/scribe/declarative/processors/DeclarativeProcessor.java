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
package com.karuslabs.scribe.declarative.processors;

import com.google.auto.service.AutoService;

import com.karuslabs.annotations.processors.AnnotationProcessor;
import com.karuslabs.scribe.*;
import com.karuslabs.scribe.declarative.Plugin;
import com.karuslabs.scribe.declarative.resolvers.*;

import java.util.*;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;


@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedAnnotationTypes("com.karuslabs.scribe.declarative.Configuration")
public class DeclarativeProcessor extends AnnotationProcessor {
    
    TypeMirror type;
    List<Resolver<Plugin>> resolvers;
    YAMLWriter writer;
    
    
    public DeclarativeProcessor() {
        resolvers = new ArrayList<>();
    }
    
    
    @Override
    public void init(ProcessingEnvironment environment) {
        super.init(environment);
        type = elements.getTypeElement(Plugin.class.getName()).asType();
        
        resolvers.add(new PluginResolver(messager));
        resolvers.add(new InformationResolver(messager));
        resolvers.add(new LoadResolver(messager));
        resolvers.add(new CommandResolver(messager));
        resolvers.add(new PermissionResolver(messager));
        
        writer = new YAMLWriter(environment.getFiler(), messager);
    }
    
    
    @Override
    protected void process(Element element) {
        if (!types.isAssignable(element.asType(), type)) {
            error(element, "Invalid annotated type: " + element.getSimpleName() + ", type must extend " + Plugin.class.getName());
            return;
        }
        
        if (element.getModifiers().contains(Modifier.ABSTRACT)) {
            error(element, "Invalid type: " + element.getSimpleName() + ", type cannot be abstract");
            return;
        }

        try {
            var type = Class.forName(element.asType().toString());
            var constructor = type.getConstructor();
            constructor.setAccessible(true);
            
            var plugin = (Plugin) constructor.newInstance();
            plugin.build();
            writer.write(resolve(plugin));
            
        } catch (ClassNotFoundException e) {
            error(element, "No such class: " + element.asType() + ", class could not be loaded");
            
        } catch (ReflectiveOperationException e) {
            error(element, "Unable to instantiate: " + element.asType() + ", class must declare a constructor with no arguments");
        }
    }
    
    protected Map<String, ?> resolve(Plugin plugin) {
        var map = new HashMap<String, Object>();
        for (var resolver : resolvers) {
            resolver.resolve(plugin, map);
            resolver.close();
        }
        
        return map;
    }
    
}
