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
package com.karuslabs.commons.command.aot.resolvers;

import com.karuslabs.commons.command.OptionalContext;
import com.karuslabs.commons.command.aot.*;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import javax.lang.model.element.*;
import javax.lang.model.type.*;

import static com.karuslabs.commons.command.aot.Binding.*;
import static javax.lang.model.element.Modifier.*;


public class MethodResolver extends Resolver<ExecutableElement> {

    TypeMirror completable;
    TypeMirror context;
    TypeMirror optional;
    TypeMirror builder;
    TypeMirror exception;
    
    
    public MethodResolver(Environment environment) {
        super(environment);
        var suggestions = elements.getTypeElement(Suggestions.class.getName()).asType();
        
        completable = specialize(CompletableFuture.class, suggestions);
        context = specialize(CommandContext.class, sender);
        optional = specialize(OptionalContext.class, sender);
        exception = elements.getTypeElement(CommandSyntaxException.class.getName()).asType();
        builder = elements.getTypeElement(SuggestionsBuilder.class.getName()).asType();
    }

    
    @Override
    public void resolve(ExecutableElement method, Token token, Element location) {
        var modifiers = method.getModifiers();
        if (!modifiers.contains(PUBLIC)) {
            environment.error(method, "Method should be public");
            return;
        }
        
        if (command(method.getReturnType(), method.getParameters()) && exceptions(method.getThrownTypes())) {
            token.bind(environment, COMMAND, location);

        } else if (predicate(method.getReturnType(), method.getParameters())) {
            token.bind(environment, REQUIREMENT, location);
            
        } else if (suggestions(method.getReturnType(), method.getParameters()) && exceptions(method.getThrownTypes())) {
            token.bind(environment, SUGGESTIONS, location);
            
        } else {
            environment.error(method, "Signature should match Command<CommandSender>, Execution<CommandSender>, Predicate<CommandSender> or SuggestionProvider<CommandSender>");
        }
    }

    
    boolean command(TypeMirror type, List<? extends VariableElement> parameters) {
        if (parameters.size() < 1) {
            return false;
        }
        
        var returnable = type.getKind();
        var first = parameters.get(0).asType();
        
        return (parameters.size() == 1 && returnable == TypeKind.INT && types.isSubtype(context, first)
            || (parameters.size() == 2 && returnable == TypeKind.VOID && types.isSubtype(sender, first)) && types.isSubtype(optional, parameters.get(1).asType()));
    }

    
    boolean predicate(TypeMirror type, List<? extends VariableElement> parameters) {
        return type.getKind() == TypeKind.BOOLEAN 
            && parameters.size() == 1 
            && types.isSubtype(sender, parameters.get(0).asType());
    }
    
    
    boolean suggestions(TypeMirror type, List<? extends VariableElement> parameters) {
        return types.isSubtype(type, completable)
            && parameters.size() == 2
            && types.isSubtype(context, parameters.get(0).asType()) 
            && types.isSubtype(builder, parameters.get(1).asType());
    }
    
    
    boolean exceptions(List<? extends TypeMirror> thrown) {
        return thrown.isEmpty() || (thrown.size() == 1 && types.isSubtype(thrown.get(0), exception));
    }
    
}
