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
package com.karuslabs.scribe.core.parsers;

import com.karuslabs.annotations.Ignored;
import com.karuslabs.scribe.annotations.Plugin;
import com.karuslabs.scribe.core.Environment;

import java.util.*;
import java.util.regex.Matcher;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.*;

import static com.karuslabs.annotations.processor.Messages.*;
import static javax.lang.model.element.ElementKind.CONSTRUCTOR;
import static javax.lang.model.element.Modifier.ABSTRACT;


/**
 * A parser that transforms a {@code @Plugin} annotation into bootstrapping related key-value pairs. 
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
public abstract class PluginParser<T> extends Parser<T> {
    
    /**
     * Creates a {@code PluginParser} that parses annotations on a {@code Class<?>}.
     * 
     * @param environment the environment
     * @return a {@code PluginParser} for classes
     */
    public static PluginParser<Class<?>> type(Environment<Class<?>> environment) {
        return new ClassPluginResolver(environment);
    }
    
    /**
     * Creates a {@code PluginParser} that parses annotations on an {@link Element}.
     * 
     * @param environment the environment
     * @param elements the elements
     * @param types the types
     * @return a {@code PluginParser} for {@code Element}s.
     */
    public static PluginParser<Element> element(Environment<Element> environment, Elements elements, Types types) {
        return new ElementPluginParser(environment, elements, types);
    }
    
    
    Matcher matcher;
    
    
    /**
     * Creates a {@code PluginParser} with the given environment.
     * 
     * @param environment the environment
     */
    public PluginParser(Environment<T> environment) {
        super(environment, Set.of(Plugin.class));
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
            environment.error("Project does not contain a @Plugin annotation, should contain one @Plugin annotation");
            
        } else if (types.size() > 1) {
            for (var type : types) {
                environment.error(type, "Project contains " + types.size() + " @Plugin annotations, should contain one @Plugin annotation");
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
    protected void parse(T type) {
        var plugin = environment.resolver.any(type, Plugin.class);
        
        environment.mappings.put("main", stringify(type));
        
        var name = plugin.name().isEmpty() ? environment.project.name : plugin.name();
        if (!matcher.reset(name).matches()) {
            environment.error(type, format(name, "is not a valid plugin name", "should contain only alphanumeric characters and \"_\""));
        }
        
        environment.mappings.put("name", name);
        
        var version = plugin.version().isEmpty() ? environment.project.version : plugin.version();
        if (!VERSIONING.matcher(version).matches()) {
            environment.warn(type, format(version, "may be malformed", "version should follow SemVer, https://semver.org/"));
        }
        
        environment.mappings.put("version", version);
    }
    
    /**
     * Transforms the given type into a {@code String}.
     * 
     * @param type the annotated type
     * @return a string representation of the given type
     */
    protected abstract String stringify(T type);

}


/**
 * A {@code PluginParser} that parses annotations on a class.
 */
class ClassPluginResolver extends PluginParser<Class<?>> {
    
    static final int ABSTRACT = 0x00000400;

    
    ClassPluginResolver(Environment<Class<?>> environment) {
        super(environment);
    }
    
    
    @Override
    protected void check(Class<?> type) {
        if ((type.getModifiers() & ABSTRACT) != 0) {
            environment.error(type, format(type.getName(), "is not a valid main class", "should not be abstract"));
        }
        
        if (!org.bukkit.plugin.Plugin.class.isAssignableFrom(type)) {            
            environment.error(type, format(type.getName(), "is not a valid main class", "should inherit from " + quote(org.bukkit.plugin.Plugin.class.getName())));
        }
        
        for (var constructor : type.getDeclaredConstructors()) {
            if (constructor.getParameterCount() != 0 ) {
                environment.error(type, format(type.getName(), "is not a valid main class", "should not contain a constructor with parameters"));
            }
        }
    }

    @Override
    protected String stringify(Class<?> type) {
        return type.getName();
    }
    
}


/**
 * A {@code PluginParser} that parses annotations on a an {@code Element}.
 */
class ElementPluginParser extends PluginParser<Element> {
    
    Types types;
    TypeMirror expected;
    Visitor visitor;
    
    
    ElementPluginParser(Environment<Element> environment, Elements elements, Types types) {
        super(environment);
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
    
    
    /**
     * A visitor that checks if a class annotated with {@code @Plugin} is valid
     * and contains no constructors with parameters.
     */
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
                environment.error(element, format(element.asType(), "is not a valid main class", "should not be abstract"));
            }
            
            if (!types.isAssignable(element.asType(), expected)) {
                environment.error(element, format(element.asType(), "is not a valid main class", "should inherit from " + quote(org.bukkit.plugin.Plugin.class.getName())));
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
                environment.error(element, format(element.getEnclosingElement(), "is not a valid main class", "should not contain a constructor with parameters"));
                return false;
                
            } else {
                return true;
            }
        }
        
    }
    
}
