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
package com.karuslabs.scribe.annotations.resolvers;

import com.karuslabs.annotations.Ignored;
import com.karuslabs.scribe.annotations.Plugin;
import com.karuslabs.scribe.annotations.processor.Resolver;

import java.util.*;
import java.util.regex.Matcher;
import javax.annotation.processing.Messager;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.*;

import static javax.lang.model.element.ElementKind.CONSTRUCTOR;
import static javax.tools.Diagnostic.Kind.*;


/**
 * A resolver that transforms a {@code Plugin} annotation into bootstrapping related
 * key-value pairs.
 * 
 * The following constraints are enforced:
 * <ul>
 * <li>The existence of a <b>single</b> {@code @Plugin} annotation</li>
 * <li>The plugin name contains only alphanumeric and underscore (@code _}) characters</li>
 * <li>The main class inherits from either {@link org.bukkit.plugin.Plugin} or {@link org.bukkit.plugin.java.JavaPlugin}</li>
 * <li>The main class has no constructors with parameters</li>
 * </ul>
 * <br>
 * In addition, a compile-time warning will be issued in the following circumstances:
 * <ul>
 * <li>The version does not follow <a href = "https://semver.org/">SemVer 2.0.0</a></li>
 * </ul>
 */
public class PluginResolver extends Resolver {
    
    Types types;
    TypeMirror type;
    Visitor visitor;
    Matcher matcher;
    
    
    /**
     * Creates a {@code PluginResolver} with the given elements, types and messager.
     * 
     * @param elements the elements
     * @param types the types
     * @param messager the messager
     */
    public PluginResolver(Elements elements, Types types, Messager messager) {
        super(messager);
        this.types = types;
        type = elements.getTypeElement(org.bukkit.plugin.Plugin.class.getName()).asType();
        visitor = new Visitor();
        matcher = WORD.matcher("");
    }
    
    
    /**
     * Determines if the given elements satisfy the constraints.
     * 
     * @param elements the elements
     * @return {@code true} if all elements satisfy the constraints; else {@code false}
     */
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
    
    /**
     * Processes and adds the element to the results.
     * 
     * @param element the element
     * @param results the results
     */
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
    
    
    /**
     * A recursive descent visitor that determines if a type and its constructors
     * satisfy the constraints. Nested types are ignored.
     */
    public class Visitor extends SimpleElementVisitor9<Boolean, Boolean> {
        
        /**
         * Creates a {@code Visitor}.
         */
        public Visitor() {
            super(true);
        }
        
        
        /**
         * Determines if the given type and its constructors satisfy the constraints.
         * 
         * @param element the type to be visited
         * @param nested whether the given type is nested within another type
         * @return {@code true} if the type is nested or satisfies the constraints;
         *         else {@code false}
         */
        @Override
        public Boolean visitType(TypeElement element, Boolean nested) {
            if (nested) {
                return true;
            }
            
            var valid = true;
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
        
        /**
         * Determines if the executable is a constructor and satisfies the constraints.
         * 
         * @param element the executable to be visited
         * @param nested ignored
         * @return {@code true} if this executable is either not a constructor or a no-args
         *         constructor; else {@code false}
         */
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
