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
package com.karuslabs.puff.type;

import com.karuslabs.annotations.*;

import javax.lang.model.element.*;
import javax.lang.model.util.SimpleElementVisitor9;

import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class Find<T extends Element> extends SimpleElementVisitor9<T, Void> {
    
    public static final Find<ExecutableElement> EXECUTABLE = new FindExecutable();
    public static final Find<TypeElement> TYPE = new FindType();
    public static final Find<PackageElement> PACKAGE = new FindPackage();
    public static final Find<ModuleElement> MODULE = new FindModule();
    
    @Override
    protected @Nullable T defaultAction(Element element, @Ignored Void parameter) {
        var enclosing = element.getEnclosingElement();
        return enclosing == null ? DEFAULT_VALUE : enclosing.accept(this, null);
    }
    
}

class FindExecutable extends Find<ExecutableElement> {
    
    @Override
    public @Nullable ExecutableElement visitModule(ModuleElement element, @Ignored Void parameter) {
        return DEFAULT_VALUE;
    }
    
    @Override
    public @Nullable ExecutableElement visitPackage(PackageElement element, @Ignored Void parameter) {
        return DEFAULT_VALUE;
    }
    
    @Override
    public @Nullable ExecutableElement visitType(TypeElement element, @Ignored Void parameter) {
        return DEFAULT_VALUE;
    }
    
    @Override
    public ExecutableElement visitExecutable(ExecutableElement element, @Ignored Void parameter) {
        return element;
    }
    
}

class FindType extends Find<TypeElement> {
    
    @Override
    public @Nullable TypeElement visitModule(ModuleElement element, @Ignored Void parameter) {
        return DEFAULT_VALUE;
    }
    
    @Override
    public @Nullable TypeElement visitPackage(PackageElement element, @Ignored Void parameter) {
        return DEFAULT_VALUE;
    }
    
    @Override
    public TypeElement visitType(TypeElement element, @Ignored Void parameter) {
        return element;
    }
    
}

class FindPackage extends Find<PackageElement> {
    
    @Override
    public @Nullable PackageElement visitModule(ModuleElement element, @Ignored Void parameter) {
        return DEFAULT_VALUE;
    }
    
    
    @Override
    public @Nullable PackageElement visitPackage(PackageElement element, @Ignored Void parameter) {
        return element;
    }
    
}

class FindModule extends Find<ModuleElement> {
    
    @Override
    public @Nullable ModuleElement visitModule(ModuleElement element, @Ignored Void parameter) {
        return element;
    }
    
}