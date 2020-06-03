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
package com.karuslabs.annotations.processor;

import com.karuslabs.annotations.*;

import javax.lang.model.element.*;
import javax.lang.model.util.SimpleElementVisitor9;

import org.checkerframework.checker.nullness.qual.Nullable;


/**
 * A filter that recursively traverses enclosing elements to find an element.
 * @param <T> the type of the resultant element
 */
public abstract class Filter<T extends Element> extends SimpleElementVisitor9<T, Void> {
    
    /**
     * A {@code Filter} that finds the enclosing type of an element.
     */
    public static final Filter<TypeElement> CLASS = new ClassFilter();
    /**
     * A {@code Filter} that finds the enclosing package of an element.
     */
    public static final Filter<PackageElement> PACKAGE = new PackageFilter();
    /**
     * A {@code Filter} that finds the enclosing module of an element.
     */
    public static final Filter<ModuleElement> MODULE = new ModuleFilter();
    
    
    /**
     * Recursively traverses the enclosed elements of the given element to find an element.
     * 
     * @param element the element
     * @param parameter an ignored parameter
     * @return {@link #DEFAULT_VALUE} or the element that was found.
     */
    @Override
    protected @Nullable T defaultAction(Element element, @Ignored Void parameter) {
        var enclosing = element.getEnclosingElement();
        return enclosing == null ? DEFAULT_VALUE : enclosing.accept(this, null);
    }
    
}

/**
 * A {@code Filter} subclass that finds the enclosing type of an element.
 */
class ClassFilter extends Filter<TypeElement> {
    
    /**
     * Returns {@link #DEFAULT_VALUE}.
     * 
     * @param element the package
     * @param parameter an ignored parameter
     * @return {@link #DEFAULT_VALUE}
     */
    @Override
    public @Nullable TypeElement visitModule(ModuleElement element, @Ignored Void parameter) {
        return DEFAULT_VALUE;
    }
    
    /**
     * Returns {@link #DEFAULT_VALUE}.
     * 
     * @param element the package
     * @param parameter an ignored parameter
     * @return {@link #DEFAULT_VALUE}
     */
    @Override
    public @Nullable TypeElement visitPackage(PackageElement element, @Ignored Void parameter) {
        return DEFAULT_VALUE;
    }
    
    /**
     * Returns the given type.
     * 
     * @param element the type
     * @param parameter an ignored parameter
     * @return the given type
     */
    @Override
    public TypeElement visitType(TypeElement element, @Ignored Void parameter) {
        return element;
    }
    
}

/**
 * A {@code Filter} subclass that finds the enclosing package of an element.
 */
class PackageFilter extends Filter<PackageElement> {
    
    /**
     * Returns {@link #DEFAULT_VALUE}.
     * 
     * @param element the package
     * @param parameter an ignored parameter
     * @return {@link #DEFAULT_VALUE}
     */
    @Override
    public @Nullable PackageElement visitModule(ModuleElement element, @Ignored Void parameter) {
        return DEFAULT_VALUE;
    }
    
    
    /**
     * Returns the given package.
     * 
     * @param element the package
     * @param parameter an ignored parameter
     * @return the given package
     */
    @Override
    public @Nullable PackageElement visitPackage(PackageElement element, @Ignored Void parameter) {
        return element;
    }
    
}

/**
 * A {@code Filter} subclass that finds the enclosing module of an element.
 */
class ModuleFilter extends Filter<ModuleElement> {
    
    /**
     * Returns the given module.
     * 
     * @param element the module
     * @param parameter an ignored parameter
     * @return the given module
     */
    @Override
    public @Nullable ModuleElement visitModule(ModuleElement element, @Ignored Void parameter) {
        return element;
    }
    
}