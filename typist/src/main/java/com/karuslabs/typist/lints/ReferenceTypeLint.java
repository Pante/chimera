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
package com.karuslabs.typist.lints;

import com.karuslabs.utilitary.Logger;
import com.karuslabs.typist.*;
import com.karuslabs.typist.Binding.*;
import com.karuslabs.utilitary.*;
import com.karuslabs.utilitary.type.Walker;

import com.mojang.brigadier.arguments.ArgumentType;

import java.util.ArrayList;
import javax.lang.model.type.*;
import javax.lang.model.util.SimpleTypeVisitor9;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A {@code Lint} which verifies that the type of a method parameter annotated with
 * {@code @Bind} is the same as the referenced argument's bound {@code ArgumentType}.
 */
public class ReferenceTypeLint extends TypeLint {
    
    private final Visitor visitor = new Visitor();
    private final Walker<TypeMirror> walker;
    private final TypeMirror argumentType;
    
    /**
     * Creates a {@code ReferenceTypeLint} with the given logger and types.
     * 
     * @param logger the logger used to report errors
     * @param types the types used to assert types
     */
    public ReferenceTypeLint(Logger logger, Types types) {
        super(logger, types);
        walker = Walker.erasuredAncestor(types);
        argumentType = types.type(ArgumentType.class);
    }

    @Override
    public void lint(Environment environment, Command command) {
        for (var binding : command.bindings().values()) {
            if (binding instanceof Method method) {
                for (var reference : method.parameters(command).values()) {
                    lint(environment, reference);
                }
            }
        }
    }
    
    /**
     * Verifies if the type parameter of the given command's {@code ArgumentType}
     * can be assigned to the given reference's type.
     * 
     * @param environment the environment
     * @param command the command
     * @param reference the reference
     */
    void lint(Environment environment, Reference reference) {
        var argument = reference.value().binding(Pattern.ARGUMENT_TYPE);
        if (argument == null) {
            return;
        }
        
        // TODO: Support intersection types & generic class parameters
        var ancestor = (DeclaredType) argument.site().asType().accept(walker, argumentType);
        var arguments = ancestor.getTypeArguments();
        if (arguments.isEmpty()) {
            logger.warn(reference.site(), "Parameter references an argument with a raw ArgumentType");
            return;
        }
        
        var error = arguments.get(0).accept(visitor, reference.site().asType());
        if (error != null) {
            var type = error.equals("Object") ?  "an Object" : "a supertype of " + error;
            logger.error(reference.site(), "Parameter should be " + type);
        }
    }
    
    /**
     * A {@code Visitor} that recursively returns a string representation of the
     * visited type if it cannot be assigned to a given method parameter's type.
     */
    class Visitor extends SimpleTypeVisitor9<String, TypeMirror> {
        
        @Override
        public @Nullable String visitTypeVariable(TypeVariable variable, TypeMirror parameter) {
            // TypeVariable#getLowerBound() is not allowed in Java since it will always be erasure to Object.
            return variable.getUpperBound().accept(this, parameter);
        }
        
        @Override
        public @Nullable String visitWildcard(WildcardType wildcard, TypeMirror parameter) {
            if (wildcard.getExtendsBound() == null) {
                return types.isSameType(types.object, parameter) ? null : "Object";
                
            } else {
                return wildcard.getExtendsBound().accept(this, parameter);
            }
        }
        
        @Override
        public @Nullable String visitIntersection(IntersectionType intersection, TypeMirror parameter) {
            var types = new ArrayList<String>();
            for (var bound : intersection.getBounds()) {
                var type = bound.accept(this, parameter);
                if (type != null) {
                    types.add(type);
                }
            }
            
            return types.isEmpty() ? null : Texts.and(types, Texts.STRING);
        }
        
        @Override
        public @Nullable String visitDeclared(DeclaredType type, TypeMirror parameter) {
            return types.isAssignable(type, parameter) ? null : type.asElement().getSimpleName().toString();
        }
        
    }
    
}
