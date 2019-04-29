/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
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
package com.karuslabs.commons.command.annotations.processors;

import com.karuslabs.annotations.processors.AnnotationProcessor;
import com.karuslabs.commons.command.DefaultableContext;

import com.google.auto.service.AutoService;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.List;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.SimpleElementVisitor9;



@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedAnnotationTypes({
    "com.karuslabs.commons.command.annotations.Literal", "com.karuslabs.commons.command.annotations.Literals",
    "com.karuslabs.commons.command.annotations.Argument", "com.karuslabs.commons.command.annotations.Arguments"
})
public class SignatureProcessor extends AnnotationProcessor {
    
    private Visitor visitor;
    private TypeMirror context;
    private TypeMirror defaultable;
    private TypeMirror exception;
    
    
    public SignatureProcessor() {
        this.visitor = new Visitor();
    }
    
    
    @Override
    public void init(ProcessingEnvironment environment) {
        super.init(environment);
        context = types.erasure(elements.getTypeElement(CommandContext.class.getName()).asType());
        defaultable = types.erasure(elements.getTypeElement(DefaultableContext.class.getName()).asType());
        exception = elements.getTypeElement(CommandSyntaxException.class.getName()).asType();
    }
    
    
    @Override
    protected void process(Element element) {
        element.accept(visitor, null);
    }
    
    
    public class Visitor extends SimpleElementVisitor9<Void, Void> {
        
        
        @Override
        public Void visitExecutable(ExecutableElement element, Void ignored) {
            var modifiers = element.getModifiers();
            if (modifiers.contains(Modifier.STATIC)) {
                error(element, "Invalid modifiers for method: " + element.getSimpleName() + ", method must not be private or static");
            }
            
            var parameters = element.getParameters();
            if (parameters.size() != 1 || !signature(element.getReturnType(), parameters)) {
                error(element, "Invalid signature for method: " + element.getSimpleName() + ", signature must match either CommandContext or DefaultableContext");   
            }

            return null;
        }
        
        protected boolean signature(TypeMirror type, List<? extends VariableElement> parameters) {
            var returnable = type.getKind();
            var parameter = types.erasure(parameters.get(0).asType());
            return (returnable == TypeKind.INT && types.isSameType(context, parameter))
                || (returnable == TypeKind.VOID && types.isSameType(defaultable, parameter));
        }
        
        protected boolean exceptions(List<? extends TypeMirror> thrown) {
            return thrown.isEmpty() || (thrown.size() == 1 && types.isSubtype(thrown.get(1), exception));
        }
        
    }
    
}
