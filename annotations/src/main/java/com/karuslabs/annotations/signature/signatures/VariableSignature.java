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
package com.karuslabs.annotations.signature.signatures;

import com.karuslabs.annotations.signature.Modifiers;
import com.karuslabs.annotations.signature.Type;
import com.sun.source.tree.VariableTree;

import java.util.Set;
import javax.annotation.Nullable;
import javax.lang.model.element.Modifier;

import static java.util.Collections.EMPTY_SET;


public class VariableSignature extends TypeSignature<VariableTree> {

    public VariableSignature(Set<Modifier> modifiers, Modifiers condition, Type type, @Nullable String name) {
        super(modifiers, condition, type, name);
    }

    @Override
    public boolean test(VariableTree tree) {
        return condition.match(modifiers, tree.getModifiers().getFlags()) && type.test(tree.getType()) && name == null ? true : tree.getName().contentEquals(name);
    }
    
    
    
    public static VariableBuilder builder() {
        return new VariableBuilder(new VariableSignature(EMPTY_SET, Modifiers.ANY, Type.any(), null));
    }
    
    public static class VariableBuilder extends TypeBuilder<VariableBuilder, VariableSignature> {

        private VariableBuilder(VariableSignature signature) {
            super(signature);
        }

        @Override
        protected VariableBuilder getThis() {
            return this;
        }
        
    }
    
}
