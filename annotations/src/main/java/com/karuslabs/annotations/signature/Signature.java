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

import com.sun.source.tree.ModifiersTree;
import com.sun.source.util.SimpleTreeVisitor;

import java.util.Set;
import java.util.regex.Matcher;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;


public abstract class Signature extends SimpleTreeVisitor<Boolean, Void> {
    
    private Modifiers modifiers;
    private Set<Modifier> values;
    
    
    public Signature(Modifiers modifiers, Set<Modifier> values) {
        super(false);
        this.modifiers = modifiers;
        this.values = values;
    }
    
    
    public boolean modifiers(ModifiersTree tree) {
        return modifiers.match(values, tree.getFlags());
    }
    
}
