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
package com.karuslabs.annotations.signature;

import com.karuslabs.annotations.processors.TreeProcessor;

import com.sun.source.tree.*;
import com.sun.source.util.Trees;

import javax.lang.model.type.TypeMirror;


@FunctionalInterface
public interface Reference<T extends Tree> extends Type<T> {
    
    public static Reference<IdentifierTree> exactly(String name) {
        return tree -> tree.getName().contentEquals(name);
    }
    
    public static Reference<IdentifierTree> from(TreeProcessor<?> processor, TypeMirror type) {
        return tree -> {
            Trees trees = processor.getTrees();
            TypeMirror actual = trees.getTypeMirror(trees.getPath(processor.getRoot(), tree));
            return processor.getTypes().isAssignable(type, actual);
        };
    }
    
    public static Reference<IdentifierTree> to(TreeProcessor<?> processor, TypeMirror type) {
        return tree -> {
            Trees trees = processor.getTrees();
            TypeMirror actual = trees.getTypeMirror(trees.getPath(processor.getRoot(), tree));
            return processor.getTypes().isAssignable(actual, type);
        };
    }
    
}
