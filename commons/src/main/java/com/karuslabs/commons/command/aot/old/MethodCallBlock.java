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
package com.karuslabs.commons.command.aot.old;

import com.karuslabs.smoke.Typing;
import com.karuslabs.commons.command.OptionalContext;
import com.karuslabs.commons.command.aot.old.Mirrors.Method;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.ArrayList;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.*;

import org.bukkit.command.CommandSender;

import static com.karuslabs.smoke.Messages.quote;

public class MethodCallBlock implements Block<LambdaContext, Method> {

    private final Typing typing;
    private final TypeMirror source;
    private final TypeMirror optional;
    private final TypeMirror context;
    private final TypeMirror builder;
    
    public MethodCallBlock(Typing typing) {
        this.typing = typing;
        source = typing.type(CommandSender.class);
        optional = typing.specialize(OptionalContext.class, source);
        context = typing.specialize(CommandContext.class, source);
        builder = typing.type(SuggestionsBuilder.class);
    }
    
    @Override
    public void emit(LambdaContext lambda, Method method) {
        var parameters = method.site.getParameters();
        var arguments = new ArrayList<String>(parameters.size());
        
        for (int i = 0; i < parameters.size(); i++) {
            var argument = lambda.arguments.get(i);
            if (argument != null) {
                arguments.add(argument);
           
            } else {
                arguments.add(parameter(parameters.get(i).asType()));
            }
        }
        
        var reciever = method.site.getModifiers().contains(Modifier.STATIC) ? lambda.enclosing.type : lambda.enclosing.parameter;
        if (method.site.getReturnType().getKind() != TypeKind.VOID) {
            lambda.builder.append("return ");
        }
        
        lambda.line(reciever, ".", method.site.getSimpleName(), "(", String.join(", ", arguments), ");");
    }
    
    String parameter(TypeMirror type) {
        var types = typing.types;
        if (types.isSubtype(source, type)) {
            return "source";
            
        } else if (types.isSubtype(optional, type)) {
            return "optional";
            
        } else if (types.isSubtype(context, type)) {
            return "context";
            
        } else if (types.isSubtype(builder, type)){
            return "builder";
            
        } else {
            throw new IllegalArgumentException("Unable to infer type for " + quote(type));
        }
    }

}
