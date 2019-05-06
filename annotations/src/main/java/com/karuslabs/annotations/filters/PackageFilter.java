/*
 * The MIT License
 *
 * Copyright 2019 Matthias.
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
package com.karuslabs.annotations.filters;

import com.karuslabs.annotations.Ignored;

import javax.lang.model.element.*;
import javax.lang.model.util.SimpleElementVisitor9;

import org.checkerframework.checker.nullness.qual.Nullable;


/**
 * A filter that recursively traverses elements to find an enclosing package.
 */
public class PackageFilter extends SimpleElementVisitor9<Element, Void> {
    
    /**
     * A {@code PackageFilter}.
     */
    public static final PackageFilter FILTER = new PackageFilter();
    
    
    /**
     * Returns {@link #DEFAULT_VALUE}.
     * 
     * @param element the module
     * @param parameter an ignored parameter
     * @return {@link #DEFAULT_VALUE}
     */
    @Override
    public @Nullable Element visitModule(ModuleElement element, @Ignored Void parameter) {
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
    public @Nullable Element visitPackage(PackageElement element, @Ignored Void parameter) {
        return element;
    }
    
    
    /**
     * Recursively visits the enclosing element if present.
     * 
     * @param element the element
     * @param parameter an ignored parameter
     * @return {@link #DEFAULT_VALUE} if no enclosing package was found; otherwise
     *         a element that represents the enclosing module
     */
    @Override
    protected @Nullable Element defaultAction(Element element, @Ignored Void parameter) {
        var enclosing = element.getEnclosingElement();
        return enclosing == null ? DEFAULT_VALUE : enclosing.accept(this, null);
    }
    
}
