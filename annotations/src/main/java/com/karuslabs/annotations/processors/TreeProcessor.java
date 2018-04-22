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
package com.karuslabs.annotations.processors;

import com.sun.source.tree.*;
import com.sun.source.util.Trees;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;

import static javax.tools.Diagnostic.Kind.*;


public abstract class TreeProcessor<T extends Tree> extends CompilationProcessor<T> {
    
    protected Trees trees;
    protected CompilationUnitTree root;
    
    
    @Override
    public synchronized void init(ProcessingEnvironment environment) {
        super.init(environment);
        trees = Trees.instance(environment);
        initialise(environment);
    }
    
    
    @Override
    protected T from(Element element) {
        root = trees.getPath(element).getCompilationUnit();
        return (T) trees.getTree(element);
    }
    
    
    @Override
    protected void error(Tree tree, String message) {
        trees.printMessage(ERROR, message, tree, root);
    }
    
    @Override
    protected void warning(Tree tree, String message) {
        trees.printMessage(WARNING, message, tree, root);
    }
    
    @Override
    protected void note(Tree tree, String message) {
        trees.printMessage(NOTE, message, tree, root);
    }

    
    public Trees getTrees() {
        return trees;
    }

    public CompilationUnitTree getRoot() {
        return root;
    }
    
}
