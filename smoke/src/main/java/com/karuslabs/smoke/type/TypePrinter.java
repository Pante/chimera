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
package com.karuslabs.smoke.type;

import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.SimpleTypeVisitor9;

public abstract class TypePrinter extends SimpleTypeVisitor9<Void, StringBuilder> {

    @Override
    public Void visitDeclared(DeclaredType type, StringBuilder builder) {
        builder.append(rawType(type));
        var arguments = type.getTypeArguments();
        if (!arguments.isEmpty()) {
            builder.append('<');
            for (int i = 0; i < arguments.size() - 1; i++) {
                arguments.get(i).accept(this, builder);
                builder.append(", ");
            }
            arguments.get(arguments.size() - 1).accept(this, builder);
            builder.append('>');
        }
        
        return null;
    }
    
    @Override
    public Void visitTypeVariable(TypeVariable variable, StringBuilder builder) {
        builder.append((TypeParameterElement) variable.asElement().getSimpleName());
        
        // We do this to ignore the default T extends Object upper bound
        var upper = variable.getUpperBound();
        if (!TypeMirrors.isType(upper, Object.class)) {
            upper.accept(this, builder.append(" extends "));
        }
        
        var lower = variable.getLowerBound();
        if (lower.getKind() != TypeKind.NULL) {
            lower.accept(this, builder.append(" super "));
        }
        
        return null;
    }
    
    @Override
    public Void visitWildcard(WildcardType type, StringBuilder builder) {
        builder.append('?');
        
        var extension = type.getExtendsBound();
        if (extension != null) {
            extension.accept(this, builder.append(" extends "));
        }
        
        var superBound = type.getSuperBound();
        if (superBound != null) {
            superBound.accept(this, builder.append(" super "));
        }
        
        return null;
    }
    
    @Override
    public Void visitIntersection(IntersectionType intersection, StringBuilder builder) {
        var bounds = intersection.getBounds();
        if (bounds.isEmpty()) {
            return null;
        }
        
        for (int i = 0; i < bounds.size() - 1; i++) {
            bounds.get(i).accept(this, builder);
            builder.append(" & ");
        }
        
        bounds.get(bounds.size() - 1).accept(this, builder);
        return null;
    }
    
    @Override
    public Void visitArray(ArrayType type, StringBuilder builder) {
        type.getComponentType().accept(this, builder);
        builder.append("[]");
        return null;
    }
    
    @Override
    public Void visitPrimitive(PrimitiveType type, StringBuilder builder) {
        builder.append(type.getKind().toString().toLowerCase());
        return null;
    }
    
    
    protected abstract String rawType(DeclaredType type);
    
}
