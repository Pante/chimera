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


/**
 * A {@code Resolver} that checks the signature of a method and if valid, binds 
 * the method to a token.
 */
public class MethodResolver extends Resolver<ExecutableElement> {

    TypeMirror completable;
    TypeMirror context;
    TypeMirror optional;
    TypeMirror builder;
    TypeMirror exception;
    
    
    /**
     * Creates a {@code MethodResolver} with the given environment.
     * 
     * @param environment the environment
     */
    public MethodResolver(Environment environment) {
        super(environment);
        var suggestions = elements.getTypeElement(Suggestions.class.getName()).asType();
        
        completable = specialize(CompletableFuture.class, suggestions);
        context = specialize(CommandContext.class, sender);
        optional = specialize(OptionalContext.class, sender);
        exception = elements.getTypeElement(CommandSyntaxException.class.getName()).asType();
        builder = elements.getTypeElement(SuggestionsBuilder.class.getName()).asType();
    }

    
    /**
     * Checks the signature of the given method and if valid, binds it to {@code token}.
     * <br><br>
     * <b>Implementation details:</b><br><br>
     * A method is considered to be valid if it is public and signature matches
     * {@code Command<CommandSender}, {@code Execution<CommandSender>}, {@code Predicate<CommandSender>} 
     * or {@code SuggestionProvider<CommandSender>}.
     * 
     * @param method the method
     * @param token the token
     */
    @Override
    public void resolve(ExecutableElement method, Token token) {
        var modifiers = method.getModifiers();
        if (!modifiers.contains(PUBLIC)) {
            environment.error(method, "Method should be public");
            return;
        }
        
        if (command(method.getReturnType(), method.getParameters()) && exceptions(method.getThrownTypes())) {
            token.bind(environment, COMMAND, method);

        } else if (predicate(method.getReturnType(), method.getParameters())) {
            token.bind(environment, REQUIREMENT, method);
            
        } else if (suggestions(method.getReturnType(), method.getParameters()) && exceptions(method.getThrownTypes())) {
            token.bind(environment, SUGGESTIONS, method);
            
        } else {
            environment.error(method, "Signature should match Command<CommandSender>, Execution<CommandSender>, Predicate<CommandSender> or SuggestionProvider<CommandSender>");
        }
    }

    
    /**
     * Returns whether the given return type and parameters matches that of a {@code Command<CommandSender>}
     * or {@code Execution<CommandSender>}.
     * 
     * @param type the return type
     * @param parameters the parameters
     * @return {@code true} if the given return type and parameters match
     */
    boolean command(TypeMirror type, List<? extends VariableElement> parameters) {
        if (parameters.size() < 1) {
            return false;
        }
        
        var returnable = type.getKind();
        var first = parameters.get(0).asType();
        
        return (parameters.size() == 1 && returnable == TypeKind.INT && types.isSubtype(context, first)
            || (parameters.size() == 2 && returnable == TypeKind.VOID && types.isSubtype(sender, first)) && types.isSubtype(optional, parameters.get(1).asType()));
    }

    
    /**
     * Returns whether the given return type and parameters match that of a 
     * {@code Predicate<CommandSender>}.
     * 
     * @param type the return type
     * @param parameters the parameters
     * @return {@code true} if the given return type and parameters match
     */
    boolean predicate(TypeMirror type, List<? extends VariableElement> parameters) {
        return type.getKind() == TypeKind.BOOLEAN 
            && parameters.size() == 1 
            && types.isSubtype(sender, parameters.get(0).asType());
    }
    
    
    /**
     * Returns whether the given return type and parameters match that of a 
     * {@code SuggestionProvider<CommandSender>}.
     * 
     * @param type the return type
     * @param parameters the parameters
     * @return {@code true} if the given return type and parameters match
     */
    boolean suggestions(TypeMirror type, List<? extends VariableElement> parameters) {
        return types.isSubtype(type, completable)
            && parameters.size() == 2
            && types.isSubtype(context, parameters.get(0).asType()) 
            && types.isSubtype(builder, parameters.get(1).asType());
    }
    
    
    /**
     * Returns whether the given thrown exceptions is empty or contains a single 
     * {@code CommandSyntaxExcetpion}.
     * 
     * @param thrown the exceptions that were thrown
     * @return {@code true} if {@code thrown} is empty or contains a single {@code CommandSyntaxException}
     */
    boolean exceptions(List<? extends TypeMirror> thrown) {
        return thrown.isEmpty() || (thrown.size() == 1 && types.isSubtype(thrown.get(0), exception));
    }
    
}
