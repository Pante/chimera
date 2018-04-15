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
import static com.sun.source.tree.Tree.Kind.PARAMETERIZED_TYPE;

import javax.annotation.processing.*;


@SupportedAnnotationTypes({
    "com.karuslabs.annotations.AAA"
})
public class ValueBasedProcessor extends TreeProcessor<ClassTree> {

    @Override
    protected void process(ClassTree tree) {
        error(tree, "KIND: " + tree.getTypeParameters().get(0).getBounds().get(0).getKind());
        for (Tree member : tree.getMembers()) {
            if (member instanceof VariableTree) {
                VariableTree field = (VariableTree) member;
                if (field.getType().getKind() == PARAMETERIZED_TYPE) {
                    ParameterizedTypeTree parameterized = (ParameterizedTypeTree) field.getType();
                    error(parameterized, "Parameterized: type: " + parameterized.getType().getKind());
                    error(parameterized, "Parameterized: arguments: " + parameterized.getTypeArguments().get(0).getKind());
                    
                } else {
                    error(field, "Field: " + field.getName() + " type: " + field.getType().getKind());
                }

            }
        }
    }

}
