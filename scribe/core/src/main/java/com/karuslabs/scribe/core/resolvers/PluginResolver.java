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


/**
 * A resolver that transforms a {@code @Plugin} annotation into bootstrapping related key-value pairs. 
 * <br>
 * <br>
 * The following constraints are enforced:
 * <ul>
 * <li>The existence of a <b>single</b> {@code @Plugin} annotation</li>
 * <li>The plugin name contains only alphanumeric and underscore ({@code _}) characters</li>
 * <li>The main class inherits from either {@link org.bukkit.plugin.Plugin} or {@link org.bukkit.plugin.java.JavaPlugin}</li>
 * <li>The main class has no constructors with parameters</li>
 * </ul>
 * <br>
 * In addition, a compile-time warning will be issued under the following circumstances:
 * <ul>
 * <li>The version does not follow <a href = "https://semver.org/">SemVer 2.0.0</a></li>
 * </ul>
 * 
 * @param <T> the annotated type
 */
public abstract class PluginResolver<T> extends Resolver<T> {
    
    /**
     * A {@code PluginResolver} that resolves annotations on a {@code Class<?>}.
     */
    public static final PluginResolver<Class<?>> CLASS = new ClassPluginResolver();
    /**
     * Creates a {@code PluginResolver} that resolves annotations on an {@link Element}.
     * 
     * @param elements the elements
     * @param types the types
     * @return a {@code PluginResolver} for {@code Element}s.
     */
    public static PluginResolver<Element> element(Elements elements, Types types) {
        return new ElementPluginResolver(elements, types);
    }
    
    
    Matcher matcher;
    
    
    /**
     * Creates a {@code PluginResolver}.
     */
    public PluginResolver() {
        super(Set.of(Plugin.class));
        matcher = WORD.matcher("");
    }
    
    
    /**
     * Determines if the number of given types and each individual type satisfy the constraints.
     * 
     * @param types the annotated types
     */
    @Override
    protected void check(Set<T> types) {
        if (types.isEmpty()) {
            resolution.error("No @Plugin annotation found, plugin must contain a @Plugin annotation");
            
        } else if (types.size() > 1) {
            for (var type : types) {
                resolution.error(type, "Invalid number of @Plugin annotations, plugin must contain only one @Plugin annotation");
            }
            
        } else {
            check(new ArrayList<>(types).get(0));
        }
    }
    
    /**
     * Determines if the given type satisfy the constraints.
     * 
     * @param type the annotated type.
     */
    protected abstract void check(T type);
    
    
    
    /**
     * Validates, processes and adds the {@code @Plugin} annotation on {@code type} to key-value pairs
     * in {@link #resolution}. Infers the {@code name} and {@code version} from
     * the respective values in {@link #project} if present.
     * 
     * @param type the annotated type
     */
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
    
    /**
     * Transforms the given type into a {@code String}.
     * 
     * @param type the annotated type
     * @return a string representation of the given type
     */
    protected abstract String stringify(T type);

}


class ClassPluginResolver extends PluginResolver<Class<?>> {
    
    static final int ABSTRACT = 0x00000400;
    
    
    @Override
    protected void check(Class<?> type) {
        if ((type.getModifiers() & ABSTRACT) != 0) {
            resolution.error(type, "Invalid main class: '" + type.getName() + "', main class cannot be abstract");
        }
        
        if (!org.bukkit.plugin.Plugin.class.isAssignableFrom(type)) {
            resolution.error(
                type, 
                "Invalid main class: '" + type.getName() + "', main class must inherit from '" + org.bukkit.plugin.Plugin.class.getName() + "'"
            );
        }
        
        for (var constructor : type.getDeclaredConstructors()) {
            if (constructor.getParameterCount() != 0 ) {
                resolution.error(type, "Invalid main class: '" + type.getName() + "', main class cannot contain constructors with parameters");
            }
        }
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
    protected void check(Element type) {
        type.accept(visitor, false);
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
