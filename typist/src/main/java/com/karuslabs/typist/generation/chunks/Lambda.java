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
package com.karuslabs.typist.generation.chunks;

import com.karuslabs.typist.*;
import com.karuslabs.typist.Binding.*;
import com.karuslabs.utilitary.Source;

import java.util.*;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import static com.karuslabs.utilitary.Texts.quote;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * A generator which emits a lambda expression that adapts a method annotated with 
 * {@code Bind} to a target functional interface's signature.
 */
@FunctionalInterface
public interface Lambda {
    
    /**
     * Creates a {@code Lambda} generator for methods annotated with {@code Bind}
     * that represent a {@code Command}.
     * 
     * @param types the types
     * @param counter a counter used to make local variable names unique
     * @return a {@code Lambda}
     */
    static Lambda command(Types types, int[] counter) {
        return new Closure(types, counter, Map.of("context", types.context), true);
    }
    
    /**
     * Creates a {@code Lambda} generator for methods annotated with {@code Bind}
     * that represent an {@code Execution}.
     * 
     * @param types the types
     * @param counter a counter used to make local variable names unique
     * @return a {@code Lambda}
     */
    static Lambda execution(Types types, int[] counter) {
        var parameters = new LinkedHashMap<String, TypeMirror>();
        parameters.put("sender", types.sender);
        parameters.put("context", types.optionalContext);
        
        return new Closure(types, counter, parameters, false);
    }
    
    /**
     * A generator which emits a method reference to a method annotated with {@code Bind}.
     * 
     * This generator should be used if a the method annotated with {@code Bind} 
     * already conforms to a target functional interface's signature.
     */
    static final Lambda METHOD_REFERENCE = (Source source, Command command, Method method, String terminator) -> {
        var reciever = method.site().getModifiers().contains(STATIC) ? command.site().getQualifiedName() : Constants.SOURCE;
        source.line(reciever, "::", method.site().getSimpleName(), terminator);
    };
    
    
    /**
     * Emits a lambda expression using the given command and method.
     * 
     * @param source the source to which the generated lambda expression is appended
     * @param command the command
     * @param method the method annotated with {@code @Bind}
     * @param terminator the terminating string for the generated lambda expression
     */
    public void emit(Source source, Command command, Method method, String terminator);
    
}

/**
 * A {@code Closure} emits a lambda expression that adapts a method annotated with 
 * {@code Bind} to a target functional interface's signature.
 */
record Closure(Types types, int[] counter, Map<String, TypeMirror> parameters, boolean returns) implements Lambda {
    
    @Override
    public void emit(Source source, Command command, Method method, String terminator) {
        var reciever = method.site().getModifiers().contains(STATIC) ? command.site().getQualifiedName() : Constants.SOURCE;
        var references = method.parameters(command);
        
        source.line(Source.arguments(parameters.keySet()), " -> {")
          .indent()
            .line(returns ? "return " : "", reciever, ".", method.site().getSimpleName(), Source.arguments(desugar(source, command, references, method.site().getParameters())), ";")
          .unindent()
          .line("}", terminator);
    }
    
    /**
     * Resolves the given method parameters and generates statements to retrieve 
     * arguments for those annotated with {@code @Let}.
     * 
     * @param source the source to which the generated lambda expression is appended
     * @param command the command
     * @param references the references to arguments
     * @param siteParameters a method's parameters
     * @return the generated local variables 
     */
    List<String> desugar(Source source, Command command, Map<Integer, Reference> references, List<? extends VariableElement> siteParameters) {
        var variables = new ArrayList<String>();
        
        for (int i = 0; i < siteParameters.size(); i++) {
            var reference = references.get(i);
            if (reference == null) {
                variables.add(match(siteParameters.get(i)));
                        
            } else {
                var name = reference.value().identity().name() + counter[0]++;
                
                variables.add(name);
                source.assign(
                    name, 
                    "context.getArgument" + Source.arguments(quote(reference.value().identity().name()), 
                    types.erasure(reference.site().asType()) + ".class")
                );
            }
        }
        
        return variables;
    }
    
    /**
     * Returns the name of the generated closure's parameter which type matches
     * the given method parameter's.
     * 
     * @param parameter the method parameter
     * @return the name of the matching generated closure parameter
     * @throws IllegalArgumentException if the given method parameter does match 
     *                                  any generated closure parameters
     */
    String match(VariableElement parameter) {
        for (var entry : parameters.entrySet()) {
            if (types.isSubtype(entry.getValue(), parameter.asType())) {
                return entry.getKey();
            }
        }
        
        throw new IllegalStateException("Unable to resolve parameter: " + parameter.asType() + " " + parameter.getSimpleName());
    }
    
}
