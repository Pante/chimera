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
package com.karuslabs.scribe.standalone.resolvers;

import com.karuslabs.annotations.Ignored;
import com.karuslabs.scribe.annotations.Plugin;
import com.karuslabs.scribe.standalone.Resolver;

import java.util.*;
import java.util.regex.Matcher;
import javax.annotation.processing.Messager;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.*;

import static javax.lang.model.element.ElementKind.CONSTRUCTOR;
import static javax.lang.model.element.Modifier.ABSTRACT;
import static javax.tools.Diagnostic.Kind.*;


public class PluginResolver extends Resolver {
    
    Types types;
    TypeMirror type;
    Visitor visitor;
    Matcher matcher;
    
    
    public PluginResolver(Elements elements, Types types, Messager messager) {
        super(messager);
        this.types = types;
        type = elements.getTypeElement(org.bukkit.plugin.Plugin.class.getName()).asType();
        visitor = new Visitor();
        matcher = WORD.matcher("");
    }
    
    
    @Override
    protected boolean check(Set<? extends Element> elements) {
        if (elements.isEmpty()) {
            messager.printMessage(ERROR, "No @Plugin annotation found, plugin must contain a @Plugin annotation");
            return false;
            
        } else if (elements.size() > 1) {
            for (var element : elements) {
                messager.printMessage(ERROR, "Invalid number of @Plugin annotations, plugin must contain only one @Plugin annotation", element);
            }
            return false;
            
        } else {
            return elements.toArray(new Element[] {})[0].accept(visitor, false); 
        }
    }
    
    @Override
    protected void resolve(Element element, Map<String, Object> results) {
        var plugin = element.getAnnotation(Plugin.class);
        
        results.put("main", element.asType().toString());
        
        if (!matcher.reset(plugin.name()).matches()) {
            messager.printMessage(ERROR, "Invalid name: '" + plugin.name() + "', name must contain only alphanumeric characters and '_'", element);
        }
        results.put("name", plugin.name());
        
        if (!VERSIONING.matcher(plugin.version()).matches()) {
            messager.printMessage(
                MANDATORY_WARNING, 
                "Unconventional versioning scheme: '" + plugin.version() + "', " +
                "it is highly recommended that versions follows SemVer, https://semver.org/",
                element
            );
        }
        results.put("version", plugin.version());
    }
    
    
    public class Visitor extends SimpleElementVisitor9<Boolean, Boolean> {
        
        public Visitor() {
            super(true);
        }
        
        
        @Override
        public Boolean visitType(TypeElement element, Boolean nested) {
            if (nested) {
                return true;
            }
            
            var valid = true;
            
            if (element.getModifiers().contains(ABSTRACT)) {
                messager.printMessage(ERROR, "Invalid main class: " + element.asType() + ", main class cannot be abstract", element);
            }
            
            if (!types.isAssignable(element.asType(), type)) {
                messager.printMessage(
                    ERROR, 
                    "Invalid main class: '" + element.asType() + "', main class must inherit from '" + org.bukkit.plugin.Plugin.class.getName() + "'",
                    element
                );
                valid = false;
            }
            
            for (var enclosed : element.getEnclosedElements()) {
                valid &= enclosed.accept(this, true);
            }
            
            return valid;
        }
        
        @Override
        public Boolean visitExecutable(ExecutableElement element, @Ignored Boolean nested) {
            if (element.getKind() == CONSTRUCTOR && !element.getParameters().isEmpty()) {
                messager.printMessage(
                    ERROR, 
                    "Invalid main class: '" + element.getEnclosingElement() + "', main class cannot contain constructors with parameters",
                    element
                );
                return false;
                
            } else {
                return true;
            }
        }
        
    }
    
}
