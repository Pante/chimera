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
package com.karuslabs.scribe.core.resolvers;

import com.karuslabs.annotations.Ignored;
import com.karuslabs.scribe.annotations.Plugin;
import com.karuslabs.scribe.core.Resolver;

import java.util.*;
import java.util.regex.Matcher;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.*;

import static javax.lang.model.element.ElementKind.CONSTRUCTOR;
import static javax.lang.model.element.Modifier.ABSTRACT;



public abstract class PluginResolver<T> extends Resolver<T> {
    
    public static final PluginResolver<Class<?>> CLASS = new ClassPluginResolver();
    public static PluginResolver<Element> element(Elements elements, Types types) {
        return new ElementPluginResolver(elements, types);
    }
    
    
    Matcher matcher;
    
    
    public PluginResolver() {
        super(Set.of(Plugin.class));
        matcher = WORD.matcher("");
    }
    
    
    @Override
    protected boolean check(Set<T> types) {
        if (types.isEmpty()) {
            resolution.error("No @Plugin annotation found, plugin must contain a @Plugin annotation");
            return false;
            
        } else if (types.size() > 1) {
            for (var type : types) {
                resolution.error(type, "Invalid number of @Plugin annotations, plugin must contain only one @Plugin annotation");
            }
            return false;
            
        } else {
            return check(new ArrayList<>(types).get(0));
        }
    }
    
    protected abstract boolean check(T type);
    
    
    
    @Override
    protected void resolve(T type) {
        var plugin = extractor.single(type, Plugin.class);
        
        resolution.mappings.put("main", stringify(type));
        
        var name = plugin.name().isEmpty() ? project.name : plugin.name();
        if (!matcher.reset(name).matches()) {
            resolution.error(type, "Invalid name: '" + name + "', name must contain only alphanumeric characters and '_'");
        }
        
        resolution.mappings.put("name", name);
        
        var version = plugin.version().isEmpty() ? project.version : plugin.version();
        if (!VERSIONING.matcher(version).matches()) {
            resolution.warning(
                type, 
                "Unconventional versioning scheme: '" + version + "', " +
                "it is highly recommended that versions follows SemVer, https://semver.org/"
            );
        }
        
        resolution.mappings.put("version", version);
    }
    
    protected abstract String stringify(T type);

}


class ClassPluginResolver extends PluginResolver<Class<?>> {
    
    static final int ABSTRACT = 0x00000400;
    
    
    @Override
    protected boolean check(Class<?> type) {
        var valid = true;
        
        if ((type.getModifiers() & ABSTRACT) != 0) {
            resolution.error(type, "Invalid main class: '" + type.getName() + "', main class cannot be abstract");
            valid = false;
        }
        
        if (!org.bukkit.plugin.Plugin.class.isAssignableFrom(type)) {
            resolution.error(
                type, 
                "Invalid main class: '" + type.getName() + "', main class must inherit from '" + org.bukkit.plugin.Plugin.class.getName() + "'"
            );
            valid = false;
        }
        
        for (var constructor : type.getDeclaredConstructors()) {
            if (constructor.getParameterCount() != 0 ) {
                resolution.error(type, "Invalid main class: '" + type.getName() + "', main class cannot contain constructors with parameters");
                valid = false;
            }
        }
        
        return valid;
    }

    @Override
    protected String stringify(Class<?> type) {
        return type.getName();
    }
    
}


class ElementPluginResolver extends PluginResolver<Element> {
    
    Types types;
    TypeMirror expected;
    Visitor visitor;
    
    
    ElementPluginResolver(Elements elements, Types types) {
        this.types = types;
        expected = elements.getTypeElement(org.bukkit.plugin.Plugin.class.getName()).asType();
        visitor = new Visitor();
    }
    
    
    @Override
    protected boolean check(Element type) {
        return type.accept(visitor, false);
    }

    @Override
    protected String stringify(Element type) {
        return type.asType().toString();
    }
    
    
    class Visitor extends SimpleElementVisitor9<Boolean, Boolean> {
        
        Visitor() {
            super(true);
        }
        
        
        @Override
        public Boolean visitType(TypeElement element, Boolean nested) {
            if (nested) {
                return true;
            }
            
            var valid = true;
            
            if (element.getModifiers().contains(ABSTRACT)) {
                resolution.error(element, "Invalid main class: '" + element.asType() + "', main class cannot be abstract");
            }
            
            if (!types.isAssignable(element.asType(), expected)) {
                resolution.error(
                    element, 
                    "Invalid main class: '" + element.asType() + "', main class must inherit from '" + org.bukkit.plugin.Plugin.class.getName() + "'"
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
                resolution.error(
                    element, 
                    "Invalid main class: '" + element.getEnclosingElement() + "', main class cannot contain constructors with parameters"
                );
                return false;
                
            } else {
                return true;
            }
        }
        
    }
    
}
